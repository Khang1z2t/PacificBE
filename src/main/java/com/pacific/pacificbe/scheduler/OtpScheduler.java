package com.pacific.pacificbe.scheduler;

import com.pacific.pacificbe.model.Otp;
import com.pacific.pacificbe.repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OtpScheduler {
    private final OtpRepository otpRepository;

    @Scheduled(fixedRate = 300000) // 5 phút
    public void updateOtp() {
        // Xóa tất cả OTP đã hết hạn
        otpRepository.deleteByExpiresAtBefore(Instant.now());
    }
}
