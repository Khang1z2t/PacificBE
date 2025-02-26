package com.pacific.pacificbe.services;

import com.pacific.pacificbe.utils.enums.FolderType;
import org.springframework.web.multipart.MultipartFile;

public interface GoogleDriveService {
    String uploadImageToDrive(MultipartFile file, FolderType type);
}
