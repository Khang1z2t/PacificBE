package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.response.ImageData;

public interface ImageService {
    byte[] getImage(String fileId);
}
