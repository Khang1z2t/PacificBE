package com.pacific.pacificbe.listener;

import com.pacific.pacificbe.event.GuideStatusChangedEvent;
import com.pacific.pacificbe.model.Guide;
import com.pacific.pacificbe.model.TourDetail;
import com.pacific.pacificbe.repository.GuideRepository;
import com.pacific.pacificbe.utils.enums.GuideStatus;
import com.pacific.pacificbe.utils.enums.TourDetailStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GuideStatusEventListener {

    private final GuideRepository guideRepository;

    @Async
    @EventListener
    @Transactional
    public void handleGuideStatusEvent(GuideStatusChangedEvent event) {
        TourDetail tourDetail = event.getTourDetail();
        Guide guide = tourDetail.getGuide();
        if (guide == null) {
            log.warn("Guide is null for TourDetail: {}", tourDetail);
            return;
        }

        if (tourDetail.getStatus().equals(TourDetailStatus.IN_PROGRESS.toString())) {
            guide.setStatus(GuideStatus.BUSY.toString());
            guideRepository.save(guide);
        } else if (tourDetail.getStatus().equals(TourDetailStatus.CLOSED.toString())) {
            boolean isGuideAvailable = guide.getTourDetails().stream()
                    .anyMatch(d -> d.getStatus().equals(TourDetailStatus.IN_PROGRESS.toString()));
            if (!isGuideAvailable) {
                guide.setStatus(GuideStatus.AVAILABLE.toString());
                guideRepository.save(guide);
            }
        }
        log.info("Received GuideStatusEvent: {}", event);
    }
}
