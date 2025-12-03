package com.pacific.pacificbe.scheduler;

import com.pacific.pacificbe.dto.ImageUploadJob;
import com.pacific.pacificbe.model.Image;
import com.pacific.pacificbe.repository.ImageRepository;
import com.pacific.pacificbe.repository.TourRepository;
import com.pacific.pacificbe.services.GoogleDriveService;
import com.pacific.pacificbe.services.SupabaseService;
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

import java.io.File;
import java.io.FileInputStream;
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
    private final SupabaseService supabaseService;
    private volatile boolean running = true;

    @PostConstruct
    public void startWorker() {
        new Thread(this::processQueue, "Redis-Queue-Consumer").start();
    }

    @PreDestroy
    public void stopWorker() {
        running = false;
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }

    private void processQueue() {
        while (running) {
            try {
                // 1. Lấy Job từ Redis
                // Lưu ý: Nếu job chứa byte[], nó sẽ ngốn RAM lúc deserialization
                ImageUploadJob job = (ImageUploadJob) redisTemplate.opsForList().rightPop(Constant.IMAGE_QUEUE, 5, TimeUnit.SECONDS);

                if (job != null) {
                    // 2. Đẩy việc nặng cho Executor (Để xử lý song song 5 ảnh cùng lúc)
                    executor.submit(() -> processImageUpload(job));
                }
                // Không cần sleep ở đây vì rightPop đã block 5s nếu rỗng

            } catch (Exception e) {
                if (!running || (e.getMessage() != null && e.getMessage().contains("STOPPING"))) {
                    log.info("Redis worker đang dừng lại (Application Shutdown).");
                    break; // Thoát vòng lặp while ngay lập tức
                }

                // Nếu là lỗi khác thì mới log ERROR
                log.error("Lỗi loop queue: {}", e.getMessage());

                // Sleep nhẹ để tránh spam log nếu Redis chết thật sự
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    private void processImageUpload(ImageUploadJob job) {
        File tempFile = new File(job.getFilePath()); // Tìm lại file từ đường dẫn

        try {
            // Kiểm tra xem file còn tồn tại không
            if (!tempFile.exists()) {
                log.error("File tạm đã bị xóa mất: {}", job.getFilePath());
                return;
            }

            // --- Tái tạo MultipartFile từ File trên đĩa ---
            // Bạn cần dùng MockMultipartFile hoặc tự viết convert,
            // nhưng SupabaseService của bạn (bản tối ưu nãy) nên nhận File hoặc FileInputStream thì tốt hơn.
            // Ở đây giả sử bạn chuyển đổi để dùng lại code cũ:
            FileInputStream input = new FileInputStream(tempFile);
            MultipartFile multipartFile = new MockMultipartFile(
                    "file", job.getOriginalFileName(), "image/jpeg", input
            );

            // 1. Upload lên Supabase
            String imageUrl = supabaseService.uploadImage(multipartFile, FolderType.TOUR, true);

            // 2. Lưu vào DB (Logic cũ)
            Image newImage = new Image();
            newImage.setId(idUtil.generateId());
            newImage.setImageUrl(imageUrl);

            var tour = tourRepository.findById(job.getTourId()).orElse(null);
            if (tour != null) {
                newImage.setTour(tour);
                imageRepository.save(newImage);
            }

        } catch (Exception e) {
            log.error("Lỗi upload: {}", e.getMessage());
            handleRetry(job);
        } finally {
            // --- QUAN TRỌNG: Xóa file tạm trên đĩa sau khi xong việc ---
            if (tempFile.exists()) {
                tempFile.delete();
                log.info("Đã xóa file tạm: {}", tempFile.getName());
            }
        }
    }

    private void handleRetry(ImageUploadJob job) {
        if (job.getRetryCount() < 3) {
            job.setRetryCount(job.getRetryCount() + 1);
            log.info("Retry job lần thứ {}", job.getRetryCount());
            // Đẩy lại vào đầu hàng đợi để xử lý lại (hoặc cuối hàng đợi tùy chiến thuật)
            // Mẹo: Nên sleep một chút hoặc đẩy vào "Retry Queue" riêng để tránh spam lỗi liên tục
            try {
                Thread.sleep(1000); // Backoff đơn giản
                redisTemplate.opsForList().leftPush(Constant.IMAGE_QUEUE, job);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        } else {
            log.error("Job thất bại sau 3 lần retry. Bỏ qua: {}", job.getOriginalFileName());
            // TODO: Gửi thông báo lỗi cho user hoặc lưu vào bảng "FailedJobs"
        }
    }
}