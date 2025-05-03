package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.config.VNPAYConfig;
import com.pacific.pacificbe.dto.request.CheckOutRequest;
import com.pacific.pacificbe.dto.request.VNPAYRequest;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.model.*;
import com.pacific.pacificbe.repository.*;
import com.pacific.pacificbe.services.MailService;
import com.pacific.pacificbe.services.PaymentService;
import com.pacific.pacificbe.services.VNPAYService;
import com.pacific.pacificbe.utils.*;
import com.pacific.pacificbe.utils.enums.BookingStatus;
import com.pacific.pacificbe.utils.enums.PaymentStatus;
import com.pacific.pacificbe.utils.enums.WalletStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class VNPAYServiceImpl implements VNPAYService {
    private final VNPAYConfig vnpayConfig;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;
    private final MailService mailService;
    private final QrUtil qrUtil;
    private final CalendarUtil calendarUtil;
    private final SystemWalletRepository systemWalletRepository;
    private final TransactionRepository transactionRepository;
    private final IdUtil idUtil;
    private final AuthUtils authUtils;

    @Override
    public String createOrder(HttpServletRequest request, VNPAYRequest vnpayRequest, String redirectTo) {
        if (vnpayRequest.getAmount() <= 0) {
            return "/";
        }
        redirectTo = authUtils.getRedirectUrl(redirectTo);
        String state = Base64.getUrlEncoder().encodeToString(redirectTo.getBytes());
        String userId = AuthUtils.getCurrentUserId();
        String baseUrl = vnpayRequest.getUrlReturn();
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = request.getScheme() + "://" + request.getServerName();
            if (request.getServerPort() != 80 && request.getServerPort() != 443) {
                baseUrl += ":" + request.getServerPort();
            }
            vnpayRequest.setUrlReturn(baseUrl);
        }
        String orderInfo = vnpayRequest.getOrderInfo();
        String bookingNo = orderInfo;
        if (orderInfo != null && !orderInfo.contains("|")) {
            orderInfo = userId + "|" + orderInfo;
            vnpayRequest.setOrderInfo(orderInfo);
        } else if (orderInfo != null && orderInfo.contains("|")) {
            String[] parts = orderInfo.split("\\|");
            if (parts.length >= 2) {
                bookingNo = parts[1];
            }
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Booking booking = bookingRepository.findByBookingNo(bookingNo)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        // Tạo vnp_TxnRef duy nhất cho mỗi lần thanh toán
        Payment payment = paymentRepository.findByBooking_IdAndStatus(booking.getId(), PaymentStatus.PENDING.toString())
                .orElse(null);
        String vnp_TxnRef = null;

        if (payment != null && payment.getExpireAt().isAfter(LocalDateTime.now())) {
            // Sử dụng Payment PENDING hiện tại
            vnp_TxnRef = payment.getTxnRef();
            log.info("Sử dụng Payment PENDING {} với vnp_TxnRef {} cho booking {}",
                    payment.getId(), vnp_TxnRef, bookingNo);
        } else {
            // Đánh dấu Payment cũ là EXPIRED nếu có
            if (payment != null) {
                payment.setStatus(PaymentStatus.EXPIRED.toString());
                payment.setUpdatedAt(LocalDateTime.now());
                paymentRepository.save(payment);
                log.info("Đánh dấu Payment {} là EXPIRED cho booking {}", payment.getId(), bookingNo);
            }

            // Tạo Payment mới
            vnp_TxnRef = bookingNo + "_" + System.currentTimeMillis();
            if (paymentRepository.existsByTxnRef(vnp_TxnRef)) {
                throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
            }

            payment = new Payment();
            payment.setCreatedAt(LocalDateTime.now());
            payment.setUser(user);
            payment.setBooking(booking);
            payment.setTotalAmount(new BigDecimal(vnpayRequest.getAmount()));
            payment.setStatus(PaymentStatus.PENDING.toString());
            payment.setTxnRef(vnp_TxnRef);
            payment.setPaymentMethod("VNPAY");

            payment.setExpireAt(LocalDateTime.now().plusMinutes(30));
            paymentRepository.save(payment);

            // Cập nhật Payment hiện tại cho Booking
            booking.setPayment(payment);
            bookingRepository.save(booking);

            log.info("Tạo Payment mới {} với vnp_TxnRef {} cho booking {}",
                    payment.getId(), vnp_TxnRef, bookingNo);
        }

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_IpAddr = vnpayConfig.getIpAddress(request);
        String vnp_TmnCode = vnpayConfig.vnp_TmnCode;
        String orderType = "order-type";

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(vnpayRequest.getAmount() * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnpayRequest.getOrderInfo() + "|" + vnp_TxnRef + "|" + state);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");

        String urlReturn = vnpayRequest.getUrlReturn() + vnpayConfig.vnp_Returnurl;
        vnp_Params.put("vnp_ReturnUrl", urlReturn);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 30);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        log.info("VNPAY parameters: vnp_TxnRef: {}, vnp_Amount: {}, vnp_OrderInfo: {}, vnp_ReturnUrl: {}, vnp_CreateDate: {}, vnp_ExpireDate: {}",
                vnp_TxnRef, vnp_Params.get("vnp_Amount"), vnp_Params.get("vnp_OrderInfo"), vnp_Params.get("vnp_ReturnUrl"),
                vnp_CreateDate, vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (Iterator<String> itr = fieldNames.iterator(); itr.hasNext(); ) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);

            if (fieldValue != null && !fieldValue.isEmpty()) {
                String encodedFieldName = URLEncoder.encode(fieldName, StandardCharsets.US_ASCII);
                String encodedFieldValue = URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII);

                hashData.append(fieldName).append('=').append(encodedFieldValue);
                query.append(encodedFieldName).append('=').append(encodedFieldValue);

                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = query.toString();
        String salt = vnpayConfig.vnp_HashSecret;
        String vnp_SecureHash = vnpayConfig.hmacSHA512(salt, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        log.info("Generated VNPAY payment URL: {}", vnpayConfig.vnp_PayUrl + "?" + queryUrl);
        return vnpayConfig.vnp_PayUrl + "?" + queryUrl;
    }

    @Override
    @Transactional
    public int orderReturn(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();

        request.getParameterNames().asIterator().forEachRemaining(param -> {
            String encodedName = URLEncoder.encode(param, StandardCharsets.US_ASCII);
            String encodedValue = URLEncoder.encode(request.getParameter(param), StandardCharsets.US_ASCII);
            if (!encodedValue.isEmpty()) {
                fields.put(encodedName, encodedValue);
            }
        });

        // Xóa các tham số không cần thiết
        fields.computeIfPresent("vnp_SecureHashType", (k, v) -> null);
        fields.computeIfPresent("vnp_SecureHash", (k, v) -> null);

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        String signValue = vnpayConfig.hashAllFields(fields);

        if (!signValue.equals(vnp_SecureHash)) {
            return -1; // Chữ ký không hợp lệ
        }

        String vnp_TransactionStatus = request.getParameter("vnp_TransactionStatus");

        return "00".equals(vnp_TransactionStatus) ? 1 : 0;
    }

    // Phương thức nội bộ để xử lý callback
    private RedirectView handleVnpayReturn(CheckOutRequest request) {
        return paymentService.callBackPayment(request);
    }

    //    Thay thế callBackPayment của PaymentServiceImpl
    @Override
    @Transactional
    public RedirectView processVnpayReturn(CheckOutRequest request) {
        String orderInfo = request.getVnp_OrderInfo();
        String[] parts = orderInfo.split("\\|");
        String userId = parts[0];
        String bookingNo = parts[1];
        String vnp_TxnRef = parts[2];
        String state = parts[3];
        String stateUrl = new String(Base64.getUrlDecoder().decode(state));
        String redirectTo = authUtils.getRedirectUrl(stateUrl);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
//        tối giản code, hay vì return lỗi ở dưới thì return ở trên
        Booking booking = bookingRepository.findByBookingNo(bookingNo)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));
        Payment payment = paymentRepository.findByTxnRef(vnp_TxnRef).orElseThrow(
                () -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));

        String lastTransactionId = transactionRepository.findLatestWalletTransactionIdOfToday();
        BigDecimal paymentAmount = new BigDecimal(request.getVnp_Amount())
                .divide(new BigDecimal(100), RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);

        boolean isSuccess = "00".equals(request.getVnp_ResponseCode());
        if (isSuccess) {
            payment.setStatus(PaymentStatus.COMPLETED.toString());
            payment.setNote(request.getVnp_TransactionNo());
            payment.setTransactionId(idUtil.generateTransactionId(lastTransactionId));
        } else {
            payment.setStatus(payment.getExpireAt().isAfter(LocalDateTime.now())
                    ? PaymentStatus.PENDING.toString()
                    : PaymentStatus.EXPIRED.toString());
        }
        paymentRepository.save(payment);
        String redirectBaseUrl = isSuccess ? redirectTo + UrlMapping.PAYMENT_SUCCESS :
                redirectTo + UrlMapping.PAYMENT_FAIL;
        String url = UriComponentsBuilder.fromUriString(redirectBaseUrl)
                .queryParam("orderNo", bookingNo)
                .build().toUriString();
        if (isSuccess) {
            SystemWallet systemWallet = systemWalletRepository.findById("SYSTEM_WALLET")
                    .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));
            // Cập nhật số dư cho system_wallet
            systemWallet.setBalance(systemWallet.getBalance().add(paymentAmount));
            systemWallet.setUpdatedAt(LocalDateTime.now());
            systemWalletRepository.save(systemWallet);

            // Ghi log giao dịch vào wallet_transaction
            Transaction transaction = new Transaction();
            transaction.setId(idUtil.generateTransactionId(lastTransactionId));
            transaction.setWallet(systemWallet);
            transaction.setBooking(booking);
            transaction.setUser(user);
            transaction.setAmount(paymentAmount);
            transaction.setType(WalletStatus.WITHDRAW.toString());
            transaction.setStatus(WalletStatus.COMPLETED.toString());
            transaction.setDescription("Thanh toán: " + bookingNo);
            transactionRepository.saveAndFlush(transaction);

//            Transaction Save -> Booking Save
            booking.setTransaction(transaction);
            booking.setStatus(BookingStatus.PAID.toString());
            bookingRepository.save(booking);
            // Gửi email xác nhận thanh toán thành công
            String qrCodeData = booking.getBookingNo();
            byte[] qrCodeBytes = qrUtil.generateQRCode(qrCodeData, 200, 200);

            Map<String, byte[]> attachments = new HashMap<>();
            attachments.put("qrCode.png", qrCodeBytes);

            mailService.queueEmail(user.getEmail(),
                    "Xác nhận thanh toán thành công cho booking: " + booking.getBookingNo(),
                    getBookingConfirm(booking));
            mailService.queueEmail(user.getEmail(),
                    "Vé tham gia của booking: " + booking.getBookingNo(),
                    getBookingTicket(booking),
                    attachments);
            log.info("Gửi email xác nhận thanh toán thành công cho booking: {}", booking.getBookingNo());
        }
        return new RedirectView(url);
    }

    private String getBookingConfirm(Booking booking) {
        try {
            byte[] bytes = FileCopyUtils.copyToByteArray(
                    new ClassPathResource("mail/booking_confirm.html").getInputStream());
            String emailBody = new String(bytes, StandardCharsets.UTF_8);

            emailBody = emailBody.replace("{{homePageUrl}}", UrlMapping.FE_URL);
            emailBody = emailBody.replace("{{firstName}}", booking.getUser().getFirstName());
            emailBody = emailBody.replace("{{lastName}}", booking.getUser().getLastName());
            emailBody = emailBody.replace("{{bookingNo}}", booking.getBookingNo());

            // Formatter cho ngày (không có giờ)
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            // Formatter cho giờ
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            TourDetail tourDetail = booking.getTourDetail();
            emailBody = emailBody.replace("{{tourTitle}}", tourDetail.getTour().getTitle());
            emailBody = emailBody.replace("{{startTime}}", tourDetail.getStartDate().format(timeFormatter));
            emailBody = emailBody.replace("{{startDate}}", tourDetail.getStartDate().format(dateFormatter));
            emailBody = emailBody.replace("{{endDate}}", tourDetail.getEndDate().format(dateFormatter));
            String peopleInfo;
            int childrenNum = booking.getChildrenNum() != null ? booking.getChildrenNum() : 0;
            if (childrenNum > 0) {
                peopleInfo = booking.getTotalNumber() + " (Người lớn: " + booking.getAdultNum() + ", Trẻ em: " + childrenNum + ")";
            } else {
                peopleInfo = booking.getTotalNumber() + " (Người lớn: " + booking.getAdultNum() + ")";
            }
            emailBody = emailBody.replace("{{peopleInfo}}", peopleInfo);

            BigDecimal roundedAmount = booking.getTotalAmount()
                    .divide(BigDecimal.valueOf(1000), 0, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(1000));
            emailBody = emailBody.replace("{{totalAmount}}", String.format("%,d VND", roundedAmount.longValue()));

            String bookingUrl = UrlMapping.FE_URL + "/booking/" + booking.getBookingNo();

            emailBody = emailBody.replace("{{bookingLink}}", bookingUrl);
            return emailBody;
        } catch (Exception e) {
            log.warn("Error while getting booking confirm: {}", e.getMessage());
            throw new AppException(ErrorCode.CANT_SEND_MAIL);
        }
    }

    private String getBookingTicket(Booking booking) {
        try {
            InputStream resource = new ClassPathResource("mail/booking_ticket.html").getInputStream();
            byte[] bytes = FileCopyUtils.copyToByteArray(resource);
            String emailBody = new String(bytes, StandardCharsets.UTF_8);

            emailBody = emailBody.replace("{{homePageUrl}}", UrlMapping.FE_URL);
            emailBody = emailBody.replace("{{firstName}}", booking.getUser().getFirstName());
            emailBody = emailBody.replace("{{lastName}}", booking.getUser().getLastName());

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm a");

            TourDetail tourDetail = booking.getTourDetail();
            emailBody = emailBody.replace("{{tourName}}", tourDetail.getTour().getTitle());
            emailBody = emailBody.replace("{{startTime}}", tourDetail.getStartDate().format(timeFormatter));
            emailBody = emailBody.replace("{{startDate}}", tourDetail.getStartDate().format(dateFormatter));

            String googleCalendarUrl = calendarUtil
                    .generateGoogleCalendarLink(
                            booking.getBookingNo(),
                            tourDetail.getStartDate(),
                            tourDetail.getEndDate(),
                            booking.getTourDetail().getTour().getTitle());
            emailBody = emailBody.replace("{{calendarLink}}", googleCalendarUrl);

            emailBody = emailBody.replace("{{qrCodeUrl}}", "cid:qrCode.png");

            emailBody = emailBody.replace("{{ticketNumber}}", booking.getBookingNo());
            BigDecimal roundedAmount = booking.getTotalAmount()
                    .divide(BigDecimal.valueOf(1000), 0, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(1000));
            emailBody = emailBody.replace("{{ticketPrice}}", String.format("%,d VND", roundedAmount.longValue()));
            emailBody = emailBody.replace("{{ticketHolder}}", booking.getBookerFullName());

            return emailBody;
        } catch (Exception e) {
            log.warn("Error while getting booking ticket: {}", e.getMessage());
            throw new AppException(ErrorCode.CANT_SEND_MAIL);
        }
    }
}
