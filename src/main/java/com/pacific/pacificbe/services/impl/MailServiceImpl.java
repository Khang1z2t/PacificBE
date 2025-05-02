package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.EmailMessage;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
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

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailServiceImpl implements MailService, MailSenderInternal {
    JavaMailSender javaMailSender;
    RedisTemplate<Object, Object> redisTemplate;

//    @Value("${spring.mail.form}")
//    @NonFinal
//    private String from;

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
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // multipart=true

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true = HTML
//            helper.setFrom(from);

            // Gắn ảnh inline nếu có
            if (attachments != null && !attachments.isEmpty()) {
                for (Map.Entry<String, byte[]> entry : attachments.entrySet()) {
                    String key = entry.getKey();
                    byte[] bytes = entry.getValue();

                    // Nếu key được tham chiếu trong HTML (inline)
                    if (body.contains("cid:" + key)) {
                        helper.addInline(key, new ByteArrayResource(bytes), "image/png");
                    } else {
                        // Nếu không, coi như attachment
                        helper.addAttachment(key, new ByteArrayResource(bytes));
                    }
                }
            }

            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("Error while sending email: ", e);
            throw new AppException(ErrorCode.CANT_SEND_MAIL);
        }
    }

}
