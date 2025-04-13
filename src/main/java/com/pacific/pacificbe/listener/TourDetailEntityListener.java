package com.pacific.pacificbe.listener;

import com.pacific.pacificbe.event.GuideStatusChangedEvent;
import com.pacific.pacificbe.event.TourStatusChangedEvent;
import com.pacific.pacificbe.model.TourDetail;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TourDetailEntityListener {
    private final ApplicationEventPublisher eventPublisher;

    @PostPersist
    @PostUpdate
    @PostRemove
    @Transactional
    public void onTourDetailChange(TourDetail tourDetail) {
        eventPublisher.publishEvent(new TourStatusChangedEvent(this, tourDetail));
    }
}
