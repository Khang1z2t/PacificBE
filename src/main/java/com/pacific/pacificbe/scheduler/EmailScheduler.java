package com.pacific.pacificbe.scheduler;

import com.pacific.pacificbe.dto.EmailMessage;
import com.pacific.pacificbe.services.MailSenderInternal;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.pacific.pacificbe.utils.Constant.EMAIL_QUEUE;
import static com.pacific.pacificbe.utils.Constant.MAX_ATTEMPTS;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailScheduler {
    private final RedisTemplate<Object, Object> redisTemplate;
    private final MailSenderInternal mailSenderInternal;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10); // 10 luồng

    @Scheduled(fixedRate = 5000) // Chạy mỗi 5 giây
    public void processEmailQueue() {
        long startTime = System.currentTimeMillis();
        int processedEmails = 0;

        // Giới hạn số email xử lý trong mỗi lần chạy
        while (processedEmails < 100) {
            EmailMessage emailMessage = (EmailMessage) redisTemplate.opsForList().rightPop(EMAIL_QUEUE);
            if (emailMessage == null) {
                log.debug("Hàng đợi email trống");
                break; // Thoát nếu hàng đợi trống
            }

            processedEmails++;
            executorService.submit(() -> sendEmailWithRetry(emailMessage));
        }

        if (processedEmails > 0) {
            long duration = System.currentTimeMillis() - startTime;
            log.info("Đã xử lý {} email trong {} ms", processedEmails, duration);
        }
    }

    private void sendEmailWithRetry(EmailMessage emailMessage) {
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
                log.warn("Lỗi khi gửi email đến {} (lần {}): {}", emailMessage.getTo(), attempt, e.getMessage());
                if (attempt < MAX_ATTEMPTS) {
                    try {
                        Thread.sleep(1000L * attempt); // Backoff trước khi thử lại
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        log.error("Interrupted khi chờ thử lại email: {}", emailMessage.getTo());
                    }
                } else {
                    log.error("Đã thử {} lần, bỏ qua email: {}", MAX_ATTEMPTS, emailMessage.getTo());
                }
            }
        }
    }

    @PreDestroy
    public void shutdown() {
        log.info("Đang tắt ExecutorService...");
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                log.warn("ExecutorService không tắt hoàn toàn trong 60s, buộc tắt.");
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
            log.error("Lỗi khi tắt ExecutorService: {}", e.getMessage());
        }
    }
}
