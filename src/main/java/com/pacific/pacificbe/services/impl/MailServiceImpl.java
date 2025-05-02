package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.EmailMessage;
import com.pacific.pacificbe.dto.request.mail.BrevoEmailRequest;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.integration.mail.BrevoClient;
import com.pacific.pacificbe.services.MailSenderInternal;
import com.pacific.pacificbe.services.MailService;
import com.pacific.pacificbe.annotations.MailSchedulerOnly;
import com.pacific.pacificbe.utils.Constant;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailServiceImpl implements MailService, MailSenderInternal {
    JavaMailSender javaMailSender;
    RedisTemplate<Object, Object> redisTemplate;
    private final BrevoClient brevoClient;
    @Value("${mail.brevo.api.key}")
    @NonFinal
    private String brevoApiKey;

    @Value("${mail.form}")
    @NonFinal
    private String from;

    @Value("${mail.name}")
    @NonFinal
    private String name;

    @Override
    public void queueEmail(String to, String subject, String body) {
        queueEmail(to, subject, body, null);
    }

    @Override
    public void queueEmail(String to, String subject, String body, Map<String, byte[]> attachments) {
        EmailMessage emailMessage = new EmailMessage(to, subject, body, attachments);
        redisTemplate.opsForList().leftPush(Constant.EMAIL_QUEUE, emailMessage);
        log.info("Đã thêm vào hàng chờ cho email: {}", to);
    }

    @Override
    @MailSchedulerOnly
    public void sendEmail(String to, String subject, String body, Map<String, byte[]> attachments) {
        try {
            BrevoEmailRequest request = new BrevoEmailRequest();
            // Sử dụng địa chỉ Gmail làm sender
            request.setSender(new BrevoEmailRequest.Sender("pacific.musketeers.tni@gmail.com", "Pacific Travel"));
            request.setTo(List.of(new BrevoEmailRequest.To(to, null)));
            request.setSubject(subject);

            // Xử lý body và attachments
            Map<String, String> attachmentMap = new HashMap<>();

            if (attachments != null && !attachments.isEmpty()) {
                for (Map.Entry<String, byte[]> entry : attachments.entrySet()) {
                    String key = entry.getKey();
                    byte[] bytes = entry.getValue();

                    // Nếu body chứa cid (inline image), cần upload file trước và thay thế
                    if (body.contains("cid:" + key)) {
                        // Inline images không được hỗ trợ trực tiếp qua API trong một request
                        // Bạn cần upload file lên Brevo trước và thay thế cid bằng URL
                        log.warn("Inline images not supported directly. Consider uploading file to Brevo first.");
                        // Thêm vào attachments thay vì inline
                        attachmentMap.put(key, Base64.getEncoder().encodeToString(bytes));
                    } else {
                        // Gửi dưới dạng attachment
                        attachmentMap.put(key, Base64.getEncoder().encodeToString(bytes));
                    }
                }
                request.setAttachment(attachmentMap);
            }

            request.setHtmlContent(body);

            // Gửi email qua API
            String response = brevoClient.sendEmail(brevoApiKey, request);
            log.info("Email sent successfully: {}", response);
        } catch (Exception e) {
            log.error("Error while sending email: ", e);
            throw new AppException(ErrorCode.CANT_SEND_MAIL);
        }
    }

}
