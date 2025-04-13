package com.pacific.pacificbe.event;

import com.pacific.pacificbe.model.TourDetail;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

@Getter
public class TourStatusChangedEvent extends ApplicationEvent {
    private final TourDetail tourDetail;

    public TourStatusChangedEvent(Object source, TourDetail tourDetail) {
        super(source);
        this.tourDetail = tourDetail;
    }

    public TourStatusChangedEvent(Object source, Clock clock, TourDetail tourDetail) {
        super(source, clock);
        this.tourDetail = tourDetail;
    }
}
