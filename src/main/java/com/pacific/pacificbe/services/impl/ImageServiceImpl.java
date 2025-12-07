package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.ImageUploadJob;
import com.pacific.pacificbe.integration.google.GoogleImageClient;
import com.pacific.pacificbe.services.ImageService;
import com.pacific.pacificbe.services.SupabaseService;
import com.pacific.pacificbe.utils.ByteArrayMultipartFile;
import com.pacific.pacificbe.utils.Constant;
import com.pacific.pacificbe.utils.IdUtil;
import feign.Response;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.charset.StandardCharsets;


@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final GoogleImageClient googleImageClient;
    private final SupabaseService supabaseService;

    @Override
    @Cacheable(value = "images", key = "#fileId", unless = "#result == null")
    public byte[] getImageDrive(String fileId) {
        try {
            Response imageBytes;
            if (fileId.startsWith("ACg8oc")) {
                imageBytes = googleImageClient.getImageAvatar(fileId);
            } else {
                imageBytes = googleImageClient.getImage(fileId);
            }
            if (imageBytes == null) {
                log.warn("Image bytes are null for fileId: {}", fileId);
                return null;
            }
            log.info("Successfully fetched image for fileId: {}", fileId);
            return imageBytes.body().asInputStream().readAllBytes();
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

    @Override
    public String getImage(String id) {
        String finalUrl;

        // 2. Logic Check & Migrate
        if (id.startsWith("http")) {
            finalUrl = id;
        } else {
            // Là ID cũ của Drive -> Tạo path mới
            String supaPath = "resources/supa_" + id + ".jpg";

            // Dùng Feign Service để check
            if (supabaseService.exists(supaPath)) {
                finalUrl = supabaseService.getPublicUrl(supaPath);
            } else {
                // Chưa có -> Gọi Feign Service để Upload
                byte[] driveBytes = fetchFromGoogleDrive(id);
                if (driveBytes != null) {
                    MultipartFile multipartFile = new ByteArrayMultipartFile(
                            driveBytes,               // Dữ liệu ảnh
                            "file",                   // Tên param (form field name)
                            "supa_" + id + ".jpg", // Tên file gốc
                            "image/jpeg"              // Content-Type (giả định là jpg)
                    );
                    supabaseService.uploadImage(multipartFile, supaPath, true);
                    finalUrl = supabaseService.getPublicUrl(supaPath);
                } else {
                    return null;
                }
            }
        }

        return finalUrl;
    };

    @Override
    public MediaType getImageMediaType(String fileId) {
        try {
            Response response = googleImageClient.getImage(fileId);
            if (response == null) {
                log.warn("Response is null for fileId: {}", fileId);
                return null;
            }
            String contentType = response.headers().get("Content-Type").stream()
                    .findFirst().orElse("image/jpeg");
            return MediaType.parseMediaType(contentType);
        } catch (Exception e) {
            log.error("Error fetching image media type for fileId {}: {}", fileId, e.getMessage());
            return null;
        }
    }


    private byte[] fetchFromGoogleDrive(String fileId) {
        try {
            Response imageBytes;
            if (fileId.startsWith("ACg8oc")) {
                imageBytes = googleImageClient.getImageAvatar(fileId);
            } else {
                imageBytes = googleImageClient.getImage(fileId);
            }
            if (imageBytes == null) {
                log.warn("Image bytes are null for fileId: {}", fileId);
                return null;
            }
            log.info("Successfully fetched image for fileId: {}", fileId);
            return imageBytes.body().asInputStream().readAllBytes();
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
