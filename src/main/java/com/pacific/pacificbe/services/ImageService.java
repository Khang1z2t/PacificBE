package com.pacific.pacificbe.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;

public interface ImageService {
    byte[] getImageDrive(String fileId);

    String getImage(String request);


    MediaType getImageMediaType(String fileId);
}
