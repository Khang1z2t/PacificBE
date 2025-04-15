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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

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
    private final WalletTransactionRepository walletTransactionRepository;

    @Override
    public String createOrder(HttpServletRequest request, VNPAYRequest vnpayRequest) {
        if (vnpayRequest.getAmount() <= 0) {
            return "/";
        }

        String userId = AuthUtils.getCurrentUserId();
        if (userId == null) {
            throw new AppException(ErrorCode.USER_NOT_AUTHENTICATED);
        }

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

        // Tạo vnp_TxnRef duy nhất cho mỗi lần thanh toán
        String vnp_TxnRef = bookingNo + "_" + System.currentTimeMillis();

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
        vnp_Params.put("vnp_OrderInfo", vnpayRequest.getOrderInfo());
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");

        String urlReturn = vnpayRequest.getUrlReturn() + vnpayConfig.vnp_Returnurl;
        vnp_Params.put("vnp_ReturnUrl", urlReturn);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

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
        var user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
//        tối giản code, hay vì return lỗi ở dưới thì return ở trên
        var booking = bookingRepository.findByBookingNo(bookingNo)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        if ("00".equals(request.getVnp_ResponseCode())) {
            Payment payment = new Payment();
            payment.setTransactionId(request.getVnp_TransactionNo());
            payment.setActive(true);
            payment.setTotalAmount(
                    new BigDecimal(request.getVnp_Amount())
                            .divide(new BigDecimal(100), RoundingMode.HALF_UP)
                            .setScale(2, RoundingMode.HALF_UP)
            );
            payment.setStatus(PaymentStatus.COMPLETED.toString());
            payment.setUser(user);
            payment.setNote(bookingNo);
            paymentRepository.save(payment);

            booking.setStatus(BookingStatus.PAID.toString());
            bookingRepository.save(booking);
            // Gửi email xác nhận thanh toán thành công
            String qrCodeData = booking.getBookingNo();
            byte[] qrCodeBytes = qrUtil.generateQRCode(qrCodeData, 200, 200);

            Map<String, byte[]> attachments = new HashMap<>();
            attachments.put("qrCode", qrCodeBytes);

            mailService.queueEmail(user.getEmail(),
                    "Xác nhận thanh toán thành công cho booking: " + booking.getBookingNo(),
                    getBookingConfirm(booking));
            mailService.queueEmail(user.getEmail(),
                    "Vé tham gia của booking: " + booking.getBookingNo(),
                    getBookingTicket(booking),
                    attachments);
            log.info("Gửi email xác nhận thanh toán thành công cho booking: {}", booking.getBookingNo());
//            Gui tien vao vi system

            // Cập nhật số dư cho system_wallet
            SystemWallet systemWallet = systemWalletRepository.findById("SYSTEM_WALLET")
                    .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));
            BigDecimal paymentAmount = payment.getTotalAmount();
            systemWallet.setBalance(systemWallet.getBalance().add(paymentAmount));
            systemWallet.setUpdatedAt(LocalDateTime.now());
            systemWalletRepository.save(systemWallet);

            // Ghi log giao dịch vào wallet_transaction
            WalletTransaction transaction = new WalletTransaction();
            transaction.setId(UUID.randomUUID().toString());
            transaction.setWallet(systemWallet);
            transaction.setBooking(booking);
            transaction.setUser(user);
            transaction.setAmount(paymentAmount);
            transaction.setType("DEPOSIT");
            transaction.setStatus("COMPLETED");
            transaction.setCreatedAt(LocalDateTime.now());
            transaction.setUpdatedAt(LocalDateTime.now());
            transaction.setDescription("Deposit from payment for booking " + bookingNo);
            walletTransactionRepository.save(transaction);
            return new RedirectView(UrlMapping.PAYMENT_SUCCESS);
        } else {
//                Payment payment = new Payment();
//                payment.setTransactionId(request.getVnp_TransactionNo());
//                payment.setActive(true);
//                payment.setTotalAmount(new BigDecimal(request.getVnp_Amount()).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
//                payment.setStatus(PaymentStatus.FAILED.toString());
//                payment.setUser(user);
//                payment.setNote(bookingNo);
//                paymentRepository.save(payment);
//                booking.setStatus(BookingStatus.PENDING.toString());
//                bookingRepository.save(booking);
            return new RedirectView(UrlMapping.PAYMENT_FAIL);
        }
    }

    private String getBookingConfirm(Booking booking) {
        try {
            Path path = new ClassPathResource("mail/booking_confirm.html").getFile().toPath();
            String emailBody = Files.readString(path, StandardCharsets.UTF_8);

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
            log.warn("Error while getting booking confirm: ", e);
            throw new AppException(ErrorCode.CANT_SEND_MAIL);
        }
    }

    private String getBookingTicket(Booking booking) {
        try {
            Path path = new ClassPathResource("mail/booking_ticket.html").getFile().toPath();
            String emailBody = Files.readString(path, StandardCharsets.UTF_8);

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

            emailBody = emailBody.replace("{{qrCodeUrl}}", "cid:qrCode");

            emailBody = emailBody.replace("{{ticketNumber}}", booking.getBookingNo());
            BigDecimal roundedAmount = booking.getTotalAmount()
                    .divide(BigDecimal.valueOf(1000), 0, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(1000));
            emailBody = emailBody.replace("{{ticketPrice}}", String.format("%,d VND", roundedAmount.longValue()));
            emailBody = emailBody.replace("{{ticketHolder}}", booking.getBookerFullName());

            return emailBody;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
