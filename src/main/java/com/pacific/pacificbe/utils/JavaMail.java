package com.pacific.pacificbe.utils;

import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ByteArrayResource;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JavaMail {
    JavaMailSender javaMailSender;


    private String formatDate(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return outputFormat.format(inputFormat.parse(date));
        } catch (Exception e) {
            return date;
        }
    }

    private String formatCurrency(String amount) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return currencyFormat.format(new BigDecimal(amount));
    }


    public void sendEmail(String to, String subject, String body) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true indicates HTML content

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new AppException(ErrorCode.CANT_SEND_MAIL);
            // Handle the exception appropriately
        }
    }

//    public void sendEmail(String to, String subject, String body) {
//        MimeMessage message = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper;
//
//        try {
//            helper = new MimeMessageHelper(message, true, "UTF-8");
//            helper.setTo(to);
//            helper.setSubject(subject);
//            helper.setText(body, true); // true để hỗ trợ HTML content
//
//            javaMailSender.send(message);
//            log.info("Email đã gửi đến: {}", to);
//        } catch (MessagingException e) {
//            log.error("Lỗi khi gửi email đến {}: {}", to, e.getMessage());
//            throw new AppException(ErrorCode.CANT_SEND_MAIL);
//        }
//    }

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

    public void sendMailBooking(User user, String bookingNo, String tourName, String createDate, String totalPrice) {
        String subjectEmail = "Xác nhận đặt tour " + tourName;
        String bodyEmail = "<div style='font-family: Arial, sans-serif;'>"
                + "<h2 style='color: #2E86C1;'>Xác nhận đặt tour</h2>"
                + "<p>Xin chào <strong>" + user.getFirstName() + " " + user.getLastName() + "</strong>,</p>"
                + "<p>Cảm ơn bạn đã đặt tour <strong>" + tourName + "</strong> vào ngày <strong>" + formatDate(createDate) + "</strong>.</p>"
                + "<p>Mã đặt tour của bạn là: <strong style='color: #E74C3C;'>" + bookingNo + "</strong></p>"
                + "<p>Tổng giá trị đơn hàng là: <strong style='color: #27AE60;'>" + formatCurrency(totalPrice) + "</strong></p>"
                + "<p>Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.</p>"
                + "</div>";
        sendEmail(user.getEmail(), subjectEmail, bodyEmail);
    }
}
