package com.pacific.pacificbe.event;

import com.pacific.pacificbe.model.Booking;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

@Getter
public class BookingStatusChangedEvent extends ApplicationEvent {
    private final Booking booking;

    public BookingStatusChangedEvent(Object source, Booking booking) {
        super(source);
        this.booking = booking;
    }

    public BookingStatusChangedEvent(Object source, Clock clock, Booking booking) {
        super(source, clock);
        this.booking = booking;
    }
}
