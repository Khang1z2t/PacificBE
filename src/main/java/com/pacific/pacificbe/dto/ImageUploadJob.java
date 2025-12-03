package com.pacific.pacificbe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageUploadJob implements Serializable {
    private String tourId;
    private String filePath;
    private String originalFileName;
    private int retryCount;

}
