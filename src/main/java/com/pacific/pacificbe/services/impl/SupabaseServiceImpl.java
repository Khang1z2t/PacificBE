package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.integration.supabase.SupabaseClient;
import com.pacific.pacificbe.services.SupabaseService;
import com.pacific.pacificbe.utils.enums.FolderType;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SupabaseServiceImpl implements SupabaseService {
    private final SupabaseClient supabaseClient;
    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String supabaseBucket;

    @Override
    public String uploadImage(MultipartFile file, FolderType folderName) {
        // Tận dụng lại logic của hàm dưới, mặc định fullUrl = false
        return uploadImage(file, folderName, false);
    }

    @Override
    public String uploadImage(MultipartFile file, FolderType folder, boolean fullUrl) {
        File tempFile = null;
        File compressedFile = null;

        try {
            // 1. Nén ảnh
            tempFile = File.createTempFile("original", ".jpg");
            file.transferTo(tempFile);
            compressedFile = compressImage(tempFile, 0.7f);

            // 2. Tạo tên file & Đường dẫn
            String fileName = folder.toString().toLowerCase() + "/" + UUID.randomUUID() + ".jpg";

            // 3. Chuẩn bị dữ liệu và Upload
            String token = "Bearer " + supabaseKey;
            byte[] fileContent = Files.readAllBytes(compressedFile.toPath());

            supabaseClient.uploadFileToSupabase(
                    token,
                    "image/jpeg",
                    supabaseBucket,
                    fileName,
                    fileContent
            );

            // 4. Trả về kết quả dựa trên cờ fullUrl
            if (fullUrl) {
                return supabaseUrl + "/storage/v1/object/public/" + supabaseBucket + "/" + fileName;
            }
            return fileName;

        } catch (Exception e) {
            throw new RuntimeException("Lỗi upload Supabase: " + e.getMessage(), e);
        } finally {
            // Dọn dẹp file tạm (đã tách ra method riêng cho gọn)
            deleteTempFile(tempFile);
            deleteTempFile(compressedFile);
        }
    }

    // Helper method để code finally nhìn gọn hơn
    private void deleteTempFile(File file) {
        if (file != null && file.exists()) {
            try {
                file.delete();
            } catch (Exception ignored) {
                // Có thể log warning nếu cần, nhưng thường thì ignore ở finally
            }
        }
    }

    private File compressImage(File inputFile, float quality) throws IOException {
        // Tạo file tạm cho ảnh nén
        File compressedFile = File.createTempFile("compressed", ".jpg");

        // Nén ảnh bằng Thumbnailator
        Thumbnails.of(inputFile)
                .scale(1.0) // Giữ kích thước gốc
                .outputQuality(quality) // Chất lượng nén
                .outputFormat("jpg") // Định dạng JPEG
                .toFile(compressedFile);

        return compressedFile;
    }

}
