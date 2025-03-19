package com.pacific.pacificbe.services;

import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${upload.path}") // Cấu hình đường dẫn lưu file trong application.properties
    private String uploadDir;

    public String storeFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new AppException(ErrorCode.FILE_NOT_FOUND);
            }

            // Tạo tên file ngẫu nhiên để tránh trùng lặp
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);

            // Lưu file vào thư mục chỉ định
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            // Trả về đường dẫn của file đã lưu
            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }
}
