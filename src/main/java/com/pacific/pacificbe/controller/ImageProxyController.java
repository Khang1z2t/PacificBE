package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.integration.google.GoogleImageClient;
import com.pacific.pacificbe.services.ImageService;
import com.pacific.pacificbe.utils.UrlMapping;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ImageProxyController {
    private final GoogleImageClient googleImageClient;
    private final ImageService imageService;

    @GetMapping(UrlMapping.PROXY_IMAGE)
    @Retry(name = "imageRetry")
    public ResponseEntity<byte[]> proxyImage(@PathVariable String fileId) {
        try {
            var imageData = imageService.getImage(fileId);
//            var mediaType = imageService.getImageMediaType(fileId);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .header("Cache-Control", "max-age=86400")
                    .body(imageData);

        } catch (Exception e) {
            if (e.getMessage().contains("Too Many Requests") || (e.getCause() != null && e.getCause().getMessage().contains("429"))) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(null);
            }
            if (e.getMessage().contains("Image not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            log.warn("Error fetching image: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
