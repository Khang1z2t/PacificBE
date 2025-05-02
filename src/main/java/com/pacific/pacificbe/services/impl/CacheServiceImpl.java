package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.response.tour.TourResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.TourMapper;
import com.pacific.pacificbe.model.Tour;
import com.pacific.pacificbe.repository.TourRepository;
import com.pacific.pacificbe.services.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

    private final RedisCacheManager cacheManager;
    private final TourRepository tourRepository;
    private final TourMapper tourMapper;

    @Override
    @CacheEvict(value = "allTours", allEntries = true)
    public void evictAllToursCache() {
        log.info("Xóa cache allTours");
    }

    @Override
    public void evictTourById(String tourId) {
        Cache allToursCache = cacheManager.getCache("allTours");
        if (allToursCache == null) {
            log.warn("Cache 'allTours' not found");
            return;
        }

        Object nativeCache = allToursCache.getNativeCache();
        if (nativeCache instanceof Map) {
            for (Object key : ((Map<?, ?>) nativeCache).keySet()) {
                Cache.ValueWrapper valueWrapper = allToursCache.get(key);
                if (valueWrapper != null) {
                    Object cachedValue = valueWrapper.get();
                    if (cachedValue instanceof List<?>) {
                        @SuppressWarnings("unchecked")
                        List<TourResponse> tours = (List<TourResponse>) cachedValue;

                        // Find and update the tour with the given tourId
                        for (int i = 0; i < tours.size(); i++) {
                            TourResponse tour = tours.get(i);
                            if (tour.getId().equals(tourId)) {
                                // Fetch the updated tour from the database
                                Tour updatedTour = tourRepository.findById(tourId)
                                        .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION,
                                                "Tour not found in cache"));
                                TourResponse updatedTourResponse = tourMapper.toTourResponse(updatedTour);
                                // Replace the old tour with the updated one
                                tours.set(i, updatedTourResponse);
                                break;
                            }
                        }

                        allToursCache.put(key, tours);
                    }
                }
            }
        }
    }
}
