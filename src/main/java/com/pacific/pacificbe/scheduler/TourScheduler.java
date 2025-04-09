package com.pacific.pacificbe.scheduler;

import com.pacific.pacificbe.model.TourDetail;
import com.pacific.pacificbe.repository.TourDetailRepository;
import com.pacific.pacificbe.utils.enums.TourDetailStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TourScheduler {

    private final TourDetailRepository tourDetailRepository;

    @Scheduled(fixedDelay = 900000)
    @Transactional
    public void updateTourDetailStatus() {
        LocalDateTime now = LocalDateTime.now();
        log.info("Starting TourDetail status update at {}", now);

        int totalUpdated = 0;
        try {
            totalUpdated += tourDetailRepository.updateOpenToInProgress(now);
            totalUpdated += tourDetailRepository.updateOpenToClosed(now);
            totalUpdated += tourDetailRepository.updateInProgressToClosed(now);
            totalUpdated += tourDetailRepository.updateClosedToOpen(now);
            totalUpdated += tourDetailRepository.updateCanceledToOpen(now);

            log.info("Completed TourDetail status update. Total updated: {}", totalUpdated);

        } catch (Exception e) {
            log.error("Error during TourDetail status update: {}", e.getMessage());
        }
    }

}
