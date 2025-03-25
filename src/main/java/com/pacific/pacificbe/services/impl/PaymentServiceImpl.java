package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.CheckOutRequest;
import com.pacific.pacificbe.dto.response.PaymentResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.PaymentMapper;
import com.pacific.pacificbe.model.Payment;
import com.pacific.pacificbe.repository.PaymentRepository;
import com.pacific.pacificbe.repository.UserRepository;
import com.pacific.pacificbe.services.PaymentService;
import com.pacific.pacificbe.utils.AuthUtils;
import com.pacific.pacificbe.utils.UrlMapping;
import com.pacific.pacificbe.utils.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @Override
    public RedirectView callBackPayment(CheckOutRequest rq) {
        String orderInfo = rq.getVnp_OrderInfo();
        if(orderInfo == null || !orderInfo.contains("|")){
            throw new AppException(ErrorCode.INVALID_ORDER_INFO);
        }
        String[] orderData = orderInfo.split("\\|");
        if(orderData.length < 2){
            throw new AppException(ErrorCode.INVALID_ORDER_INFO);
        }
        String userId = orderData[0];
        var user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ORDER_INFO));

        String orderInfoData = orderData[1];
        Payment p = new Payment();
        if ("00".equals(rq.getVnp_ResponseCode())) {
            p.setTransactionId(rq.getVnp_TransactionNo());
            p.setActive(true);
            p.setTotalAmount(new BigDecimal(rq.getVnp_Amount()).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
            p.setStatus(PaymentStatus.COMPLETED.toString());
            p.setUser(user);
            p.setNote(orderInfoData);
            paymentRepository.save(p);
            return new RedirectView(UrlMapping.PAYMENT_SUCCESS);
        } else {
            return new RedirectView(UrlMapping.PAYMENT_FAIL);
        }
    }
}
