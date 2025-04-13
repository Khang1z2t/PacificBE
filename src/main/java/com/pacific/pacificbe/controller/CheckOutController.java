package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.request.CheckOutRequest;
import com.pacific.pacificbe.dto.request.VNPAYRequest;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.repository.BookingRepository;
import com.pacific.pacificbe.repository.UserRepository;
import com.pacific.pacificbe.services.PaymentService;
import com.pacific.pacificbe.services.VNPAYService;
import com.pacific.pacificbe.services.impl.VNPAYServiceImpl;
import com.pacific.pacificbe.utils.AuthUtils;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RequestMapping(UrlMapping.BOOKINGS)
@RestController
@RequiredArgsConstructor
public class CheckOutController {
    private final VNPAYService vnPayService;
    private final PaymentService ps;
    private final UserRepository userRepository;

    @GetMapping(UrlMapping.CHECKOUT_BOOKING)
    @Operation(summary = "Thanh toán tour")
    public String checkoutTour(@RequestParam("amount") double orderTotal,
                               @RequestParam("orderInfo") String orderInfo,
                               HttpServletRequest request) {
        int amount = (int) Math.round(orderTotal);
        VNPAYRequest vnpayRequest = VNPAYRequest.builder()
                .amount(amount)
                .orderInfo(orderInfo) // Gửi orderInfo trực tiếp, logic ghép userId sẽ xử lý trong service
                .urlReturn("") // Để trống, service sẽ tự tạo baseUrl
                .build();
        return vnPayService.createOrder(request, vnpayRequest);
    }

    @GetMapping(UrlMapping.CHECKOUT_RETURN)
    @Operation(summary = "Trả về kết quả thanh toán từ VNPAY")
    public RedirectView handleVnpayReturn(@ModelAttribute CheckOutRequest request) {
        return vnPayService.processVnpayReturn(request);
    }
}
