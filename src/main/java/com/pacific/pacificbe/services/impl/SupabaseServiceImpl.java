package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.integration.supabase.SupabaseClient;
import com.pacific.pacificbe.services.SupabaseService;
import com.pacific.pacificbe.utils.enums.FolderType;
import feign.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.UUID;

@Slf4j
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
    public String uploadImage(MultipartFile file, String customPath, boolean fullPath) {
        return uploadImage(file, null, customPath, fullPath);
    }

    @Override
    public String uploadImage(MultipartFile file, FolderType folder, boolean fullUrl) {
        return uploadImage(file, folder, null, fullUrl);
    }

    @Override
    public boolean exists(String filePath) {
        try {
            // Gọi method HEAD
            Response response = supabaseClient.checkFileExists(supabaseBucket, filePath);

            // Kiểm tra status code thủ công
            // Supabase trả về 200 nếu có, 404 nếu không
            boolean exists = response.status() == 200;

            // Đóng response để tránh memory leak (quan trọng với Feign)
            response.close();

            return exists;
        } catch (feign.FeignException.NotFound e) {
            return false; // Lỗi 404 -> Chưa có
        } catch (Exception e) {
            log.error("Error checking Supabase file: {}", e.getMessage());
            return false; // Lỗi khác coi như chưa có để an toàn
        }
    }

    @Override
    public String getPublicUrl(String filePath) {
        return supabaseUrl + "/storage/v1/object/public/" + supabaseBucket + "/" + filePath;
    }

    @Override
    public void deleteImage(String filePath) {
        try {
            String cleanPath = extractPathFromUrl(filePath);
            if (cleanPath == null || cleanPath.isEmpty()) {
                log.warn("Invalid file path for deletion: {}", filePath);
                return;
            }

            URI uri = createUri(cleanPath, false);
            String token = "Bearer " + supabaseKey;
            Response response = supabaseClient.deleteFileFromSupabase(uri, token);
            response.close();
        } catch (Exception e) {
            log.error("Error deleting Supabase file: {}", e.getMessage());
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

    private String uploadImage(MultipartFile file, FolderType folder, String customPath, boolean fullUrl) {
        File tempFile = null;
        File compressedFile = null;

        try {
            // 1. Nén ảnh
            tempFile = File.createTempFile("original", ".jpg");
            file.transferTo(tempFile);
            compressedFile = compressImage(tempFile, 0.7f);

            // 2. Tạo tên file & Đường dẫn
            String fileName = null;

            if (customPath != null && folder == null) {
                fileName = customPath;
            } else if (customPath == null && folder != null) {
                fileName = folder.toString().toLowerCase() + "/supa_" + UUID.randomUUID() + ".jpg";
            }

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

    private String extractPathFromUrl(String url) {
        // Nếu chuỗi không chứa http -> coi như nó là path sẵn rồi
        if (!url.startsWith("http")) return url;

        // Logic cắt chuỗi dựa trên tên bucket
        // URL mẫu: .../storage/v1/object/public/{bucketName}/{path/to/file}
        String keyword = "/public/" + supabaseBucket + "/";
        int index = url.indexOf(keyword);

        if (index != -1) {
            return url.substring(index + keyword.length());
        }
        return null; // Không khớp format
    }


    private URI createUri(String path, boolean isPublic) {
        // isPublic = true dùng cho hàm checkExists (vì check link public)
        // isPublic = false dùng cho upload/delete (api management)

        String type = isPublic ? "/storage/v1/object/public/" : "/storage/v1/object/";

        // Nối chuỗi thủ công để giữ nguyên dấu "/"
        String urlString = supabaseUrl + type + supabaseBucket + "/" + path;

        return URI.create(urlString);
    }

}
