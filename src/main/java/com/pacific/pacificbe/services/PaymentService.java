package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.response.PaymentResponse;
import com.pacific.pacificbe.model.Payment;

import java.util.List;

public interface PaymentService {
    void savePayment(Payment payment);
    List<PaymentResponse> getAllPayments();
}
