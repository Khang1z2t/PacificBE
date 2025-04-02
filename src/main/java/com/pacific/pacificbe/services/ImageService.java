package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.response.ImageData;
import org.springframework.http.MediaType;

public interface ImageService {
    byte[] getImage(String fileId);

    MediaType getImageMediaType(String fileId);
}
