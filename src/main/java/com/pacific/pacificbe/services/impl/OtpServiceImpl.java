package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;

import com.pacific.pacificbe.model.User;
import com.pacific.pacificbe.repository.UserRepository;
import com.pacific.pacificbe.services.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.pacific.pacificbe.utils.Constant.OTP_KEY;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OtpServiceImpl implements OtpService {
    UserRepository userRepository;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional
    public String generateOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        String otp = String.format("%06d", new Random().nextInt(999999));
        String key = OTP_KEY + user.getEmail();
        stringRedisTemplate.opsForValue().set(
                key,
                otp,
                30 * 60,
                TimeUnit.SECONDS
        );
        return otp;
    }

    @Override
    @Transactional
    public boolean verifyOtp(String email, String otp) {
        String storedOtp = stringRedisTemplate.opsForValue().get(OTP_KEY + email);
        if (storedOtp != null && storedOtp.equals(otp)) {
            stringRedisTemplate.delete(OTP_KEY + email);
            return true;
        }
        return false;
    }
}
