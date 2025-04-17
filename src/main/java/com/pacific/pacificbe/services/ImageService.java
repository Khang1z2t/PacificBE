package com.pacific.pacificbe.services;

import org.springframework.http.MediaType;

public interface ImageService {
    byte[] getImage(String fileId);

    MediaType getImageMediaType(String fileId);
}
