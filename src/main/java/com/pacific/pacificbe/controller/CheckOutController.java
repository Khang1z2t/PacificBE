package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.request.CheckOutRequest;
import com.pacific.pacificbe.dto.request.VNPAYRequest;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.repository.UserRepository;
import com.pacific.pacificbe.services.PaymentService;
import com.pacific.pacificbe.services.VNPAYService;
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
    public String checkoutTour(@RequestParam("amount") int orderTotal,
                               @RequestParam("orderInfo") String orderInfo,
                               @RequestParam("TdID") String TdID,
                               HttpServletRequest request) {
        if (orderTotal <= 0) {
            return "/";
        }



        String userId = AuthUtils.getCurrentUserId();
        if (userId == null) {
            throw new AppException(ErrorCode.NEED_LOGIN);
        }

        String baseUrl = request.getScheme() + "://" + request.getServerName();
        if (request.getServerPort() != 80 && request.getServerPort() != 443) {
            baseUrl += ":" + request.getServerPort();
        }
        VNPAYRequest vnPayRequest = VNPAYRequest.builder()
                .amount(orderTotal)
                .orderInfo(userId + "|" + orderInfo)
                .urlReturn(baseUrl)
                .build();


        return vnPayService.createOrder(request, vnPayRequest);
    }

    @GetMapping(UrlMapping.CHECKOUT_RETURN)
    @Operation(summary = "Trả về kết quả thanh toán từ VNPAY Lưu ý : OrderInfo sẽ được mã hóa base64")
    public RedirectView handleVnpayReturn(@ModelAttribute CheckOutRequest request) {
        return ps.callBackPayment(request);
    }
}
