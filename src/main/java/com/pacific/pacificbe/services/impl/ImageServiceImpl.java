package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.response.ImageData;
import com.pacific.pacificbe.integration.google.GoogleImageClient;
import com.pacific.pacificbe.services.ImageService;
import feign.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final GoogleImageClient googleImageClient;

    @Override
    @Cacheable(value = "images", key = "#fileId", unless = "#result == null")
    public byte[] getImage(String fileId) {
        try {
            byte[] imageBytes = googleImageClient.getImage(fileId);
            if (imageBytes == null) {
                log.warn("Image bytes are null for fileId: {}", fileId);
                return null;
            }
            log.info("Successfully fetched image for fileId: {}, size: {}", fileId, imageBytes.length);
            return imageBytes;
        } catch (Exception e) {
            log.error("Error fetching image from Google for fileId {}: {}", fileId, e.getMessage());
            if (e.getMessage().contains("Too Many Requests") || (e.getCause() != null && e.getCause().getMessage().contains("429"))) {
                throw new RuntimeException("Too Many Requests", e);
            }
            if (e.getMessage().contains("Image not found")) {
                throw new RuntimeException("Image not found", e);
            }
            throw new RuntimeException("Failed to fetch image for fileId: " + fileId, e);
        }
    }
}
