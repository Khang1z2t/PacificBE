package com.pacific.pacificbe.utils;

import com.pacific.pacificbe.dto.EmailMessage;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.model.User;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
public class MailService {
    JavaMailSender javaMailSender;
    RedisTemplate<Object, Object> redisTemplate;

    public void queueEmail(String to, String subject, String body) {
        queueEmail(to, subject, body, null);
    }

    public void queueEmail(String to, String subject, String body, Map<String, byte[]> attachments) {
        EmailMessage emailMessage = new EmailMessage(to, subject, body, attachments);
        redisTemplate.opsForList().leftPush(Constant.EMAIL_QUEUE, emailMessage);
        log.info("Đã thêm vào hàng chờ cho email: {}", to);
    }

    public void sendEmail(String to, String subject, String body) {
        sendEmail(to, subject, body, null);
    }

    public void sendEmail(String to, String subject, String body, Map<String, byte[]> attachments) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // multipart=true

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true = HTML

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


    public void sendMailVerify(User user, String otp) {
        String subjectEmail = otp + " là mã xác nhận email của bạn";
        String bodyEmail = "<h2>Xác nhận email</h2>"
                + "<p>Xin chào " + user.getFirstName() + " " + user.getLastName() + ",</p>"
                + "<p>Vui lòng sử dụng mã xác nhận bên dưới để hoàn tất việc xác nhận email:</p>"
                + "<h3 style='color:blue;'>" + otp + "</h3>"
                + "<p>Otp sẽ hết hạn trong vòng 30 phút.</p>"
                + "<p>Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.</p>";
        sendEmail(user.getEmail(), subjectEmail, bodyEmail);
    }

    public void sendMailForgotPassword(User user, String otp) {
        String subjectEmail = otp + " là mã xác nhận đổi mật khẩu của bạn";
        String bodyEmail = "<h2>Xác nhận đổi mật khẩu</h2>"
                + "<p>Xin chào " + user.getFirstName() + " " + user.getLastName() + ",</p>"
                + "<p>Vui lòng sử dụng mã xác nhận bên dưới để hoàn tất việc đổi mật khẩu:</p>"
                + "<h3 style='color:blue;'>" + otp + "</h3>"
                + "<p>Otp sẽ hết hạn trong vòng 30 phút.</p>"
                + "<p>Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.</p>";
        sendEmail(user.getEmail(), subjectEmail, bodyEmail);
    }

}
