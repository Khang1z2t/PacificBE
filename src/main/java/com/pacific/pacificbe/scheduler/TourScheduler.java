package com.pacific.pacificbe.scheduler;

import com.pacific.pacificbe.event.GuideStatusChangedEvent;
import com.pacific.pacificbe.repository.TourDetailRepository;
import com.pacific.pacificbe.utils.enums.TourDetailStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class TourScheduler {

    private final TourDetailRepository tourDetailRepository;

    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(fixedDelay = 60000) // mỗi phút
    @Transactional
    public void updateTourDetailStatus() {
        LocalDateTime now = LocalDateTime.now();
        try {
            tourDetailRepository.findByStatusAndStartDateBefore(TourDetailStatus.OPEN.toString(), now)
                    .forEach(tourDetail -> {
                        tourDetail.setStatus(TourDetailStatus.IN_PROGRESS.toString());
                        tourDetailRepository.save(tourDetail);
                        eventPublisher.publishEvent(new GuideStatusChangedEvent(this, tourDetail));
                    });

            tourDetailRepository.findByStatusAndEndDateBefore(TourDetailStatus.IN_PROGRESS.toString(), now)
                    .forEach(tourDetail -> {
                        tourDetail.setStatus(TourDetailStatus.CLOSED.toString());
                        tourDetailRepository.save(tourDetail);
                        eventPublisher.publishEvent(new GuideStatusChangedEvent(this, tourDetail));
                    });
        } catch (Exception e) {
            log.error("Error updating tour detail status: {}", e.getMessage());
        }
    }

}
