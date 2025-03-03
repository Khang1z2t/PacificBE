package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.services.VNPAYService;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping(UrlMapping.BOOKINGS)
@RestController
public class CheckOutController {

    @Autowired
    private VNPAYService vnPayService;

    @GetMapping(UrlMapping.CHECKOUT_BOOKING)
    @Operation(summary = "Thanh toán tour")
    public String checkoutTour(@RequestParam("amount") int orderTotal,
                               @RequestParam("orderInfo") String orderInfo,
                               HttpServletRequest request) {
        if(orderTotal <= 0) {
            return "/";
        }
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(request, orderTotal, orderInfo, baseUrl);
        return vnpayUrl;
    }

    @GetMapping(UrlMapping.CHECKOUT_RETURN)
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
    public ResponseEntity<?> handleVnpayReturn(@RequestParam Map<String, String> params, HttpServletResponse response) {
        String sucessUrl = "http://localhost:3000/checkout/success"; // Địa chỉ FE hiển thị kết quả thanh toán
        String failUrl = "http://localhost:3000/checkout/fail"; // Địa chỉ FE hiển thị kết quả thanh toán

        String respCode = params.get("vnp_ResponseCode");
        String frontendURL = "00".equals(respCode) ? sucessUrl : failUrl;
        // Tạo URL redirect kèm theo query params
        String redirectUrl = frontendURL + "?" + params.entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        try {
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Redirect failed");
        }

        return ResponseEntity.ok().build();
    }
}
