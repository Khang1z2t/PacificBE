package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.response.PaymentResponse;
import com.pacific.pacificbe.mapper.PaymentMapper;
import com.pacific.pacificbe.model.Payment;
import com.pacific.pacificbe.repository.PaymentRepository;
import com.pacific.pacificbe.services.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository pr;
    private final PaymentMapper pm;

    @Override
    public void savePayment(Payment payment) {
        pr.save(payment);
    }

    @Override
    public List<PaymentResponse> getAllPayments() {
        return pr.findAll().stream()
                .map(pm::toRequest)
                .toList();
    }
}
