package com.pacific.pacificbe.scheduler;

import com.pacific.pacificbe.dto.EmailMessage;
import com.pacific.pacificbe.utils.Constant;
import com.pacific.pacificbe.utils.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailScheduler {
    private final RedisTemplate<Object, Object> redisTemplate;
    private final MailService mailService;

    @Scheduled(fixedRate = 5000)
    public void processEmailQueue() {
        while (true) {
            EmailMessage emailMessage = (EmailMessage) redisTemplate.opsForList().rightPop(Constant.EMAIL_QUEUE);
            if (emailMessage == null) {
                break; // Không còn email nào trong hàng đợi
            }
            try {
                mailService.sendEmail(
                        emailMessage.getTo(),
                        emailMessage.getSubject(),
                        emailMessage.getBody(),
                        emailMessage.getAttachments());
            } catch (Exception e) {
                redisTemplate.opsForList().leftPush(Constant.EMAIL_QUEUE, emailMessage);
                log.error("Lỗi khi gửi email: {}", e.getMessage());
            }
        }
    }
}
