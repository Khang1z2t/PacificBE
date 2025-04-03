package com.pacific.pacificbe.scheduler;

import com.pacific.pacificbe.model.Booking;
import com.pacific.pacificbe.model.TourDetail;
import com.pacific.pacificbe.repository.BookingRepository;
import com.pacific.pacificbe.repository.TourDetailRepository;
import com.pacific.pacificbe.utils.JavaMail;
import com.pacific.pacificbe.utils.enums.BookingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingScheduler {

    private final BookingRepository bookingRepository;
    private final JavaMail javaMail;

    // Chạy lúc 0h mỗi ngày
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanOldBookings() {
        LocalDate yearAgo = LocalDate.now().minusYears(1);
        List<Booking> oldBookings = bookingRepository.findByStatusAndTourDetail_EndDate(
                BookingStatus.COMPLETED.toString(), yearAgo);
        bookingRepository.deleteAll(oldBookings);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Scheduled(fixedDelay = 300000)
    public void updateBookingStatus() {
        LocalDateTime now = LocalDateTime.now();
        List<String> statusList = Arrays.asList(
                BookingStatus.PENDING.toString(),
                BookingStatus.PAID.toString(),
                BookingStatus.ON_GOING.toString()
        );
        List<Booking> bookings = bookingRepository.findByStatusIn(statusList);

        for (Booking booking : bookings) {
            try {

                TourDetail tourDetail = booking.getTourDetail();
                if (tourDetail == null) {
                    log.warn("TourDetail is null for booking ID: {}", booking.getId());
                    continue; // Bỏ qua booking này
                }

                // Trường hợp 1: PENDING, chưa thanh toán
                if (booking.getStatus().equals(BookingStatus.PENDING.toString())) {
                    // Sau 24 giờ: Chuyển sang EXPIRED và gửi email thông báo
                    if (now.isAfter(booking.getCreatedAt().plusHours(24))) {
                        javaMail.sendEmail(booking.getUser().getEmail(),
                                "Thông báo về Booking: " + booking.getBookingNo(),
                                getBookingExpiredMail(booking));
                        booking.setStatus(BookingStatus.EXPIRED.toString());
                        bookingRepository.save(booking);
                        log.info("Booking hết hạn và đã được gửi mail: {}", booking.getBookingNo());
                    }
                }
                // Trường hợp 2: Đang diễn ra
                else if (now.isAfter(booking.getTourDetail().getStartDate().atStartOfDay()) &&
                        now.isBefore(booking.getTourDetail().getEndDate().atStartOfDay())) {
                    if (!booking.getStatus().equals(BookingStatus.ON_GOING.toString())) {
                        booking.setStatus(BookingStatus.ON_GOING.toString());
                        bookingRepository.save(booking);
                    }
                }
                // Trường hợp 3: Hoàn thành
                else if (now.isAfter(booking.getTourDetail().getEndDate().atStartOfDay())) {
                    if (!booking.getStatus().equals(BookingStatus.COMPLETED.toString())) {
                        booking.setStatus(BookingStatus.COMPLETED.toString());
                        bookingRepository.save(booking);
                    }
                }
            } catch (Exception e) {
                log.info("Error while schedule booking ", e);
            }
        }
    }

    @Transactional
    protected String getBookingExpiredMail(Booking booking) {
        try {
            Path path = new ClassPathResource("mail/booking_expired.html").getFile().toPath();
            String emailBody = Files.readString(path, StandardCharsets.UTF_8);

            // Formatter cho ngày (không có giờ)
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            // Formatter cho ngày và giờ (dùng cho createdAt)
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            emailBody = emailBody.replace("{{firstName}}", booking.getUser().getFirstName());
            emailBody = emailBody.replace("{{lastName}}", booking.getUser().getLastName());
            emailBody = emailBody.replace("{{bookingNo}}", booking.getBookingNo());

            TourDetail tourDetail = booking.getTourDetail();

            emailBody = emailBody.replace("{{tourTitle}}", tourDetail.getTour().getTitle());
            emailBody = emailBody.replace("{{startDate}}", tourDetail.getStartDate().format(dateFormatter));
            emailBody = emailBody.replace("{{endDate}}", tourDetail.getEndDate().format(dateFormatter));
            emailBody = emailBody.replace("{{totalNumber}}", booking.getTotalNumber().toString());
            emailBody = emailBody.replace("{{adultNum}}", booking.getAdultNum().toString());
            emailBody = emailBody.replace("{{childrenNum}}", booking.getChildrenNum().toString());

            BigDecimal roundedAmount = booking.getTotalAmount()
                    .divide(BigDecimal.valueOf(1000), 0, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(1000));
            emailBody = emailBody.replace("{{totalAmount}}", String.format("%,d VND", roundedAmount.longValue()));
            emailBody = emailBody.replace("{{deadline}}", booking.getCreatedAt().plusDays(1).format(dateTimeFormatter));

            return emailBody;
        } catch (Exception e) {
            log.error("Error generating booking expired email: {}", e.getMessage());
            return null;
        }
    }
}
