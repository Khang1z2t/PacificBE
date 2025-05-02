package com.pacific.pacificbe.listener;

import com.pacific.pacificbe.event.TourStatusChangedEvent;
import com.pacific.pacificbe.model.Tour;
import com.pacific.pacificbe.model.TourDetail;
import com.pacific.pacificbe.repository.TourRepository;
import com.pacific.pacificbe.services.CacheService;
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
    private final CacheService cacheService;

    @Async
    @EventListener
    @Transactional
    public void handleTourStatusChangedEvent(TourStatusChangedEvent event) {
        TourDetail tourDetail = event.getTourDetail();
        String tourId = tourDetail.getTour() != null ? tourDetail.getTour().getId() : null;

        if (tourId != null) {
            Tour tour = tourRepository.findById(tourId).orElse(null);
            if (tour != null) {
                tour.updateStatus();
                tourRepository.save(tour);
                cacheService.evictTourById(tourId);
                log.info("Updated Tour ID: {} status to {}", tour.getId(), tour.getStatus());
            }
        }
    }
}