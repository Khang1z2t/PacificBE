package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.CheckOutRequest;
import com.pacific.pacificbe.dto.response.PaymentResponse;
import com.pacific.pacificbe.model.Payment;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

public interface PaymentService {
    void savePayment(Payment payment);
    List<PaymentResponse> getAllPayments();
    RedirectView callBackPayment(CheckOutRequest checkOutRequest);
}
