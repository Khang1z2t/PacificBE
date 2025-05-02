package com.pacific.pacificbe.scheduler;

import com.pacific.pacificbe.event.BookingStatusChangedEvent;
import com.pacific.pacificbe.model.Booking;
import com.pacific.pacificbe.model.TourDetail;
import com.pacific.pacificbe.repository.BookingRepository;
import com.pacific.pacificbe.services.MailService;
import com.pacific.pacificbe.utils.UrlMapping;
import com.pacific.pacificbe.utils.enums.BookingStatus;
import com.pacific.pacificbe.utils.enums.TourDetailStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

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
    private final MailService mailService;
    private final ApplicationEventPublisher eventPublisher;


    // Chạy lúc 0h mỗi ngày
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void cleanOldBookings() {
//        LocalDate yearAgo = LocalDate.now().minusYears(1);
//        List<Booking> oldBookings = bookingRepository.findByStatusAndTourDetail_EndDate(
//                BookingStatus.COMPLETED.toString(), yearAgo);
//        bookingRepository.deleteAll(oldBookings);
//    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Scheduled(fixedDelay = 300000)
    public void updateBookingStatus() {
        LocalDateTime now = LocalDateTime.now();

        List<Booking> expiredBookings = bookingRepository.findByStatusAndCreatedAtBefore(
                BookingStatus.PENDING.toString(), now.minusHours(24));
        if (!expiredBookings.isEmpty()) {
            int updatedCount = bookingRepository.updateStatusToExpired(now);
            log.info("Updated {} bookings to EXPIRED", updatedCount);
            expiredBookings.forEach(booking -> {
                booking.setStatus(BookingStatus.EXPIRED.toString());
                eventPublisher.publishEvent(new BookingStatusChangedEvent(this, booking));
                mailService.queueEmail(booking.getUser().getEmail(),
                        "Thông báo về Booking: " + booking.getBookingNo(),
                        getBookingExpiredMail(booking));
                log.info("Booking expired and email sent: {}", booking.getBookingNo());
            });
        }

        List<Booking> paidBookings = bookingRepository.findByStatus(BookingStatus.PAID.toString());
        if (!paidBookings.isEmpty()) {
            int updatedCount = bookingRepository.updateStatusToOngoing();
            log.info("Updated {} bookings to ON_GOING", updatedCount);
            paidBookings.stream()
                    .filter(b -> b.getTourDetail() != null
                            && b.getTourDetail().getStatus().equals(TourDetailStatus.IN_PROGRESS.toString()))
                    .forEach(booking -> {
                        booking.setStatus(BookingStatus.ON_GOING.toString());
                        eventPublisher.publishEvent(new BookingStatusChangedEvent(this, booking));
                        log.info("Booking updated to ON_GOING: {}", booking.getBookingNo());
                    });
        }

        List<Booking> ongoingBookings = bookingRepository.findByStatus(BookingStatus.ON_GOING.toString());
        if (!ongoingBookings.isEmpty()) {
            int updatedCount = bookingRepository.updateStatusToCompleted();
            log.info("Updated {} bookings to COMPLETED", updatedCount);
            ongoingBookings.stream()
                    .filter(b -> b.getTourDetail() != null
                            && b.getTourDetail().getStatus().equals(TourDetailStatus.CLOSED.toString()))
                    .forEach(booking -> {
                        booking.setStatus(BookingStatus.COMPLETED.toString());
                        eventPublisher.publishEvent(new BookingStatusChangedEvent(this, booking));
                        log.info("Booking updated to COMPLETED: {}", booking.getBookingNo());
                    });
        }
    }

    @Transactional
    protected String getBookingExpiredMail(Booking booking) {
        try {
            byte[] bytes = FileCopyUtils.copyToByteArray(
                    new ClassPathResource("mail/booking_expired.html").getInputStream());
            String emailBody = new String(bytes, StandardCharsets.UTF_8);

            // Formatter cho ngày (không có giờ)
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            // Formatter cho ngày và giờ (dùng cho createdAt)
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            emailBody = emailBody.replace("{{homePageUrl}}", UrlMapping.FE_URL);
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
