package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.config.VNPAYConfig;
import com.pacific.pacificbe.dto.request.CheckOutRequest;
import com.pacific.pacificbe.dto.request.VNPAYRequest;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.model.Booking;
import com.pacific.pacificbe.model.Payment;
import com.pacific.pacificbe.repository.BookingRepository;
import com.pacific.pacificbe.repository.PaymentRepository;
import com.pacific.pacificbe.repository.UserRepository;
import com.pacific.pacificbe.services.PaymentService;
import com.pacific.pacificbe.services.VNPAYService;
import com.pacific.pacificbe.utils.AuthUtils;
import com.pacific.pacificbe.utils.UrlMapping;
import com.pacific.pacificbe.utils.enums.PaymentStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VNPAYServiceImpl implements VNPAYService {
    private final VNPAYConfig vnpayConfig;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

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

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName).append('=');
                try {
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString())).append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
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
        Map fields = new HashMap();
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = null;
            String fieldValue = null;
            try {
                fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }

        String signValue = vnpayConfig.hashAllFields(fields);
        if (signValue.equals(vnp_SecureHash)) {
            String vnp_TransactionStatus = request.getParameter("vnp_TransactionStatus");
            String vnp_TxnRef = request.getParameter("vnp_TxnRef");
            String bookingNo = vnp_TxnRef.split("_")[0];

            if ("00".equals(vnp_TransactionStatus)) {
                Optional<Booking> bookingOpt = bookingRepository.findByBookingNo(bookingNo);
                if (bookingOpt.isPresent()) {
                    Booking booking = bookingOpt.get();
                    booking.setStatus("CONFIRMED");
                    bookingRepository.save(booking);
                }
                return 1;
            } else {
                Optional<Booking> bookingOpt = bookingRepository.findByBookingNo(bookingNo);
                if (bookingOpt.isPresent()) {
                    Booking booking = bookingOpt.get();
                    booking.setStatus("FAILED");
                    bookingRepository.save(booking);
                }
                return 0;
            }
        } else {
            return -1;
        }
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
        var bookingOpt = bookingRepository.findByBookingNo(bookingNo);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            if ("00".equals(request.getVnp_ResponseCode())) {
                Payment payment = new Payment();
                payment.setTransactionId(request.getVnp_TransactionNo());
                payment.setActive(true);
                payment.setTotalAmount(new BigDecimal(request.getVnp_Amount()).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
                payment.setStatus(PaymentStatus.COMPLETED.toString());
                payment.setUser(user);
                payment.setNote(bookingNo);
                paymentRepository.save(payment);

                booking.setStatus("CONFIRMED");
                bookingRepository.save(booking);
                // Gửi email xác nhận thanh toán thành công


                return new RedirectView(UrlMapping.PAYMENT_SUCCESS);
            } else {
                Payment payment = new Payment();
                payment.setTransactionId(request.getVnp_TransactionNo());
                payment.setActive(true);
                payment.setTotalAmount(new BigDecimal(request.getVnp_Amount()).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
                payment.setStatus(PaymentStatus.FAILED.toString());
                payment.setUser(user);
                payment.setNote(bookingNo);
                paymentRepository.save(payment);
                booking.setStatus("PENDING");
                bookingRepository.save(booking);
                return new RedirectView(UrlMapping.PAYMENT_FAIL);
            }
        } else {
            throw new AppException(ErrorCode.BOOKING_NOT_FOUND);
        }
    }
}
