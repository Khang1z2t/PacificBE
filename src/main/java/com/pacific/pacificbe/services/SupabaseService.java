package com.pacific.pacificbe.services;

import com.pacific.pacificbe.utils.enums.FolderType;
import org.springframework.web.multipart.MultipartFile;

public interface SupabaseService {

    String uploadImage (MultipartFile file, FolderType folder);

    String uploadImage (MultipartFile file, FolderType folder, boolean fullUrl);
}
