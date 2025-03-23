package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.response.PaymentResponse;
import com.pacific.pacificbe.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

//    public Payment toEntity(PaymentRequest paymentRequest) {
//        Payment payment = new Payment();
//        payment.setId(paymentRequest.getId());
//        payment.setTotalAmount(paymentRequest.getTotalAmount());
//        payment.setCreatedAt(paymentRequest.getCreatedAt());
//        payment.setNote(paymentRequest.getNote());
//        payment.setStatus(paymentRequest.getStatus());
//        payment.setTransactionId(paymentRequest.getTransactionId());
//        return payment;
//    }

    public PaymentResponse toRequest(Payment payment) {
        PaymentResponse pr = new PaymentResponse();
        pr.setId(payment.getId());
        pr.setTotalAmount(payment.getTotalAmount());
        pr.setCreatedAt(payment.getCreatedAt());
        pr.setNote(payment.getNote());
        pr.setStatus(payment.getStatus());
        pr.setTransactionId(payment.getTransactionId());
        return pr;
    }
}
