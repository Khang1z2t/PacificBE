package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.model.Invoice;
import com.pacific.pacificbe.model.Payment;
import com.pacific.pacificbe.repository.PaymentRepository;
import com.pacific.pacificbe.services.PaymentService;
import com.pacific.pacificbe.services.VNPAYService;
import com.pacific.pacificbe.utils.AuthenUtils;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping(UrlMapping.BOOKINGS)
@RestController
@RequiredArgsConstructor
public class CheckOutController {
    private final VNPAYService vnPayService;
    private final PaymentService ps;

    @GetMapping(UrlMapping.CHECKOUT_BOOKING)
    @Operation(summary = "Thanh toán tour")
    public String checkoutTour(@RequestParam("amount") int orderTotal,
                               @RequestParam("orderInfo") String orderInfo,
                               HttpServletRequest request) {
        if (orderTotal <= 0) {
            return "/";
        }
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        return vnPayService.createOrder(request, orderTotal, orderInfo, baseUrl);
    }

    @GetMapping(UrlMapping.CHECKOUT_RETURN)
    @Operation(summary = "Trả về kết quả thanh toán từ VNPAY Lưu ý : OrderInfo sẽ được mã hóa base64")
//    public String paymentCompleted(HttpServletRequest request, Model model) {
//        int paymentStatus = vnPayService.orderReturn(request);
//        System.out.println("Payment status: " + paymentStatus);
//        String orderInfo = request.getParameter("vnp_OrderInfo");
//        String paymentTime = request.getParameter("vnp_PayDate");
//        String transactionId = request.getParameter("vnp_TransactionNo");
//        String totalPrice = request.getParameter("vnp_Amount");
//
//        model.addAttribute("orderId", orderInfo);
//        model.addAttribute("totalPrice", totalPrice);
//        model.addAttribute("paymentTime", paymentTime);
//        model.addAttribute("transactionId", transactionId);
////        END VNPAY CODE
//
////        double sotienthucte = Double.parseDouble(totalPrice) / 100;
//        // luu len LSTT
////        Lichsuthanhtoan payment = new Lichsuthanhtoan();
////        payment.setId(Integer.parseInt(transactionId));
////        payment.setSotien(sotienthucte);
////        payment.setNgaythanhtoan(LocalDate.parse(paymentTime, DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
////        payment.setHinhthucthanhtoan("VNPAY");
////        lsDAO.save(payment);
//        return "/";
//    }
    public void handleVnpayReturn(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException {
        String redirectUrl;
        Payment p = new Payment();

        if ("00".equals(params.get("vnp_ResponseCode"))) {
            p.setTransactionId(params.get("vnp_TransactionNo"));
            p.setTotalAmount(new BigDecimal(params.get("vnp_Amount")).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));

            String startDateStr = params.get("vnp_PayDate");
            if (startDateStr != null && !startDateStr.isEmpty()) {
                p.setCreatedAt(LocalDateTime.parse(startDateStr, DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            }
            p.setStatus("COMPLETED");
            p.setActive(true);
            p.setPaymentMethod("VNPAY");
            p.setNote(params.get("vnp_OrderInfo")); // Lưu kiểu base64 => FE giải mã ra
            ps.savePayment(p);

            redirectUrl = UrlMapping.PAYMENT_SUCCESS;
        } else {
            redirectUrl = UrlMapping.PAYMENT_FAIL;
        }

        System.out.println("Redirecting to: " + redirectUrl);
        response.sendRedirect(redirectUrl);
    }
}
