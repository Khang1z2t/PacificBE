package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.response.PaymentResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.PaymentMapper;
import com.pacific.pacificbe.model.Payment;
import com.pacific.pacificbe.repository.PaymentRepository;
import com.pacific.pacificbe.repository.UserRepository;
import com.pacific.pacificbe.services.PaymentService;
import com.pacific.pacificbe.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final UserRepository userRepository;

    @Override
    public void savePayment(Payment payment) {
        String userid = AuthUtils.getCurrentUserId();
        if(userid == null) {
            throw new AppException(ErrorCode.NEED_LOGIN);
        }
        var user = userRepository.findById(userid).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
        payment.setUser(user);
        paymentRepository.save(payment);
    }

    @Override
    public List<PaymentResponse> getAllPayments() {
        String userId = AuthUtils.getCurrentUserId();
        if(userId == null) {
            throw new AppException(ErrorCode.NEED_LOGIN);
        }
        var user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<Payment> payments = paymentRepository.findAllByUser(user);
        return paymentMapper.toPaymentResponseList(payments);
    }
}
