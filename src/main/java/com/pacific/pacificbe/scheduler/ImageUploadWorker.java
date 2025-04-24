package com.pacific.pacificbe.scheduler;

import com.pacific.pacificbe.dto.ImageUploadJob;
import com.pacific.pacificbe.model.Image;
import com.pacific.pacificbe.repository.ImageRepository;
import com.pacific.pacificbe.repository.TourRepository;
import com.pacific.pacificbe.services.GoogleDriveService;
import com.pacific.pacificbe.utils.Constant;
import com.pacific.pacificbe.utils.IdUtil;
import com.pacific.pacificbe.utils.enums.FolderType;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.pacific.pacificbe.utils.Constant.IMAGE_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageUploadWorker {

    private final ExecutorService executor = Executors.newFixedThreadPool(5);
    private final RedisTemplate<Object, Object> redisTemplate;
    private final IdUtil idUtil;
    private final TourRepository tourRepository;
    private final GoogleDriveService googleDriveService;
    private final ImageRepository imageRepository;
    private volatile boolean running = true;

    @PostConstruct
    public void startWorker() {
        executor.submit(this::processQueue);
    }

    @PreDestroy
    public void stopWorker() {
        running = false;
        executor.shutdown();
    }

    private void processQueue() {
        while (running) {
            try {
                ImageUploadJob job = (ImageUploadJob) redisTemplate.opsForList().rightPop(IMAGE_QUEUE, 5, TimeUnit.SECONDS);
                if (job != null) {
                    processImageUpload(job);
                } else {
                    Thread.sleep(100);
                }
            } catch (InterruptedException ie) {
                log.warn("Image upload worker interrupted: {}", ie.getMessage());
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("Lỗi xử lý công việc tải ảnh: {}", e.getMessage());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    private void processImageUpload(ImageUploadJob job) {
        try {
            // Tạo MultipartFile từ fileBytes
            byte[] fileBytes = job.getFileBytes();
            if (fileBytes == null) {
                log.error("Không có file bytes trong job");
                return;
            }

            MultipartFile multipartFile = new MockMultipartFile(
                    "file",                      // field name used in form
                    job.getOriginalFileName(),   // original file name
                    "image/jpeg",                // content type
                    fileBytes                    // content
            );

            // Tải lên Google Drive
            String imageUrl = googleDriveService.uploadImageToDrive(multipartFile, FolderType.TOUR);

            // Lưu Image
            Image newImage = new Image();
            String generatedId = idUtil.getIdImage(imageUrl);
            newImage.setId(generatedId != null ? generatedId : idUtil.generateId());
            newImage.setImageUrl(imageUrl);
            newImage.setTour(tourRepository.findById(job.getTourId()).orElse(null));
            imageRepository.save(newImage);
        } catch (Exception e) {
            log.error("Lỗi tải ảnh: {}", e.getMessage(), e);
            // Retry tối đa 3 lần
            job.setRetryCount(job.getRetryCount() + 1);
            if (job.getRetryCount() <= 3) {
                try {
                    redisTemplate.opsForList().leftPush(Constant.IMAGE_QUEUE, job);
                } catch (Exception ex) {
                    log.error("Lỗi đẩy job vào hàng đợi: {}", ex.getMessage(), ex);
                }
            }
        }
    }

}
