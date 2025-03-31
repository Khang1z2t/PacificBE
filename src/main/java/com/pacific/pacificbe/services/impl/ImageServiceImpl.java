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
            var imageBytes = googleImageClient.getImage(fileId);
            return imageBytes;
        } catch (Exception e) {
            log.error("Error fetching image from Google: {}", e.getMessage());
            if (e.getMessage().contains("Too Many Requests") || (e.getCause() != null && e.getCause().getMessage().contains("429"))) {
                throw new RuntimeException("Too Many Requests");
            }
            if (e.getMessage().contains("Image not found")) {
                throw new RuntimeException("Image not found");
            }
        }
        return null;
    }
}
