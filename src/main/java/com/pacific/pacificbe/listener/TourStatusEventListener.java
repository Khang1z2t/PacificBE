package com.pacific.pacificbe.listener;

import com.pacific.pacificbe.event.TourStatusChangedEvent;
import com.pacific.pacificbe.model.Tour;
import com.pacific.pacificbe.model.TourDetail;
import com.pacific.pacificbe.repository.TourRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TourStatusEventListener {

    private final TourRepository tourRepository;

    @Async
    @EventListener
    @Transactional
    public void handleTourStatusChangedEvent(TourStatusChangedEvent event) {
        TourDetail tourDetail = event.getTourDetail();
        Tour tour = tourDetail.getTour();
        if (tour != null) {
            tour.updateStatus();
            tourRepository.save(tour);
            log.info("Updated Tour ID: {} status to {}", tour.getId(), tour.getStatus());
        }
    }
}
