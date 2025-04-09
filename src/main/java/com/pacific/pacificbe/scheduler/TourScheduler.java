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

    @Transactional
    @Scheduled(fixedDelay = 900000)
    public void updateTourDetailStatus() {
        LocalDateTime now = LocalDateTime.now();
        log.info("Starting TourDetail status update at {}", now);

        Pageable pageable = PageRequest.of(0, 100);
        Page<TourDetail> page;
        int totalUpdated = 0;

        do {
            page = tourDetailRepository.findTourDetailsToUpdateStatus(now, pageable);
            List<TourDetail> tourDetailsToUpdate = page.getContent();
            log.info("Processing page {} with {} TourDetails", pageable.getPageNumber(), tourDetailsToUpdate.size());

            List<TourDetail> modifiedTourDetails = new ArrayList<>();

            for (TourDetail tourDetail : tourDetailsToUpdate) {
                try {
                    String currentStatus = tourDetail.getStatus();
                    String newStatus = determineStatus(tourDetail, now);

                    if (!currentStatus.equals(newStatus)) {
                        tourDetail.setStatus(newStatus);
                        modifiedTourDetails.add(tourDetail);
                    }
                } catch (Exception e) {
                    log.error("Error updating TourDetail ID {}: {}",
                            tourDetail.getId() != null ? tourDetail.getId() : "unknown", e.getMessage());
                }
            }

            if (!modifiedTourDetails.isEmpty()) {
                tourDetailRepository.saveAll(modifiedTourDetails);
                totalUpdated += modifiedTourDetails.size();
                log.info("Updated {} TourDetails in page {}", modifiedTourDetails.size(), pageable.getPageNumber());
            }

            pageable = pageable.next();
        } while (page.hasNext());

        log.info("Completed TourDetail status update. Total updated: {}", totalUpdated);
    }


    private String determineStatus(TourDetail tourDetail, LocalDateTime now) {
        LocalDateTime startDate = tourDetail.getStartDate();
        LocalDateTime endDate = tourDetail.getEndDate();
        String currentStatus = tourDetail.getStatus();

        // Kiểm tra CANCELED trước
        if (TourDetailStatus.CANCELED.toString().equals(currentStatus)) {
            return (startDate.isAfter(now) && endDate.isAfter(now))
                    ? TourDetailStatus.OPEN.toString()
                    : TourDetailStatus.CANCELED.toString();
        }

        // Các trạng thái khác
        if (endDate.isBefore(now)) {
            return TourDetailStatus.CLOSED.toString();
        }
        return startDate.isBefore(now)
                ? TourDetailStatus.IN_PROGRESS.toString()
                : TourDetailStatus.OPEN.toString();
    }
}
