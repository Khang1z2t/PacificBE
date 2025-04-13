package com.pacific.pacificbe.listener;

import com.pacific.pacificbe.event.BookingStatusChangedEvent;
import com.pacific.pacificbe.model.Booking;
import com.pacific.pacificbe.model.Voucher;
import com.pacific.pacificbe.repository.VoucherRepository;
import com.pacific.pacificbe.utils.enums.BookingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingStatusEventListener {

    private final VoucherRepository voucherRepository;

    @Async
    @EventListener
    @Transactional
    public void handleBookingStatusEvent(BookingStatusChangedEvent event) {
        Booking booking = event.getBooking();
        if (booking == null) {
            log.warn("Booking is null for BookingStatusChangedEvent: {}", event);
            return;
        }

        // handler event remaining voucher
        if ((booking.getStatus().equals(BookingStatus.EXPIRED.toString())) && booking.getVoucher() != null) {
            Voucher voucher = booking.getVoucher();
            voucher.setQuantity(voucher.getQuantity() + 1);
            voucherRepository.save(voucher);
        }
    }
}
