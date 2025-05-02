package com.pacific.pacificbe.services;

public interface CacheService {

    void evictAllToursCache();

    void evictTourById(String tourId);
}
