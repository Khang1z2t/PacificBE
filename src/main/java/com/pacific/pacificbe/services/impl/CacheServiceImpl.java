package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.services.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

    @Override
    @CacheEvict(value = "allTours", allEntries = true)
    public void evictAllToursCache() {
        log.info("Xóa cache allTours");
    }
}
