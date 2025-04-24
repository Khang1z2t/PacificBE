package com.pacific.pacificbe.scheduler;

import com.pacific.pacificbe.model.Payment;
import com.pacific.pacificbe.repository.PaymentRepository;
import com.pacific.pacificbe.utils.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentScheduler {

    private final PaymentRepository paymentRepository;

    @Scheduled(cron = "0 0 0 * * *") // Chạy vào 00:00 mỗi ngày
    @Transactional
    public void cleanupExpiredPaymentsDaily() {
        List<Payment> expiredPayments = paymentRepository.findByStatus(PaymentStatus.EXPIRED.toString());
        if (expiredPayments.isEmpty()) {
            log.info("Không có Payment EXPIRED nào để xóa");
            return;
        }

        for (Payment payment : expiredPayments) {
            paymentRepository.delete(payment);
            log.info("Xóa Payment EXPIRED {} của booking {}", payment.getId(), payment.getBooking().getBookingNo());
        }
        log.info("Đã xóa {} Payment EXPIRED", expiredPayments.size());
    }

    @Scheduled(fixedRate = 3600000) // Chạy mỗi giờ
    @Transactional
    public void cleanupExpiredPayments() {
        LocalDateTime now = LocalDateTime.now();
        List<Payment> expiredPayments = paymentRepository.findByStatusAndExpireAtBefore(
                PaymentStatus.PENDING.toString(), now);
        for (Payment payment : expiredPayments) {
            payment.setStatus(PaymentStatus.EXPIRED.toString());
            payment.setUpdatedAt(now);
            paymentRepository.save(payment);
            log.info("Đánh dấu Payment {} là EXPIRED", payment.getId());
        }
    }
}
