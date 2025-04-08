package com.pacific.pacificbe.scheduler;

import com.pacific.pacificbe.dto.EmailMessage;
import com.pacific.pacificbe.services.MailSenderInternal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.pacific.pacificbe.utils.Constant.EMAIL_QUEUE;
import static com.pacific.pacificbe.utils.Constant.MAX_ATTEMPTS;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailScheduler {
    private final RedisTemplate<Object, Object> redisTemplate;
    private final MailSenderInternal mailSenderInternal;

    @Scheduled(fixedRate = 5000)
    public void processEmailQueue() {
        while (true) {
            EmailMessage emailMessage = (EmailMessage) redisTemplate.opsForList().rightPop(EMAIL_QUEUE);
            if (emailMessage == null) {
                break; // Không còn email nào trong hàng đợi
            }

            int attempt = 0;
            boolean sentSuccessfully = false;
            while (attempt < MAX_ATTEMPTS && !sentSuccessfully) {
                try {
                    mailSenderInternal.sendEmail(
                            emailMessage.getTo(),
                            emailMessage.getSubject(),
                            emailMessage.getBody(),
                            emailMessage.getAttachments());
                    sentSuccessfully = true;
                    log.info("Đã gửi email thành công đến: {}", emailMessage.getTo());
                } catch (Exception e) {
                    attempt++;
                    log.warn("Lỗi khi gửi email (lần {}): {}", attempt, e.getMessage());
                    if (attempt == 1) {
                        redisTemplate.opsForList().leftPush(EMAIL_QUEUE, emailMessage);
                    } else if (attempt >= MAX_ATTEMPTS) {
                        log.error("Đã thử {} lần, xóa email khỏi queue: {}", MAX_ATTEMPTS, emailMessage.getTo());
                        redisTemplate.opsForList().rightPop(EMAIL_QUEUE);
                        break;
                    }
                }
            }
        }
    }
}
