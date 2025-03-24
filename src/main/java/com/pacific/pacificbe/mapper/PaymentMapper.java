package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.response.PaymentResponse;
import com.pacific.pacificbe.model.Payment;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentMapper {
    ModelMapper modelMapper;

    public PaymentResponse toPaymentResponse(Payment payment) {
        return modelMapper.map(payment, PaymentResponse.class);
    }

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

    public List<PaymentResponse> toPaymentResponseList(List<Payment> payments) {
        return payments.stream().map(this::toPaymentResponse).toList();
    }
}
