package com.pacific.pacificbe.services.impl;

import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.pacific.pacificbe.config.DriveFolderConfig;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.services.GoogleDriveService;
import com.pacific.pacificbe.utils.GoogleAuth;
import com.pacific.pacificbe.utils.enums.FolderType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class GoogleDriveServiceImpl implements GoogleDriveService {
    @Value("${google.drive.applicationName}")
    private String applicationName;
    private final GoogleAuth googleAuth;
    private final DriveFolderConfig driveFolderConfig;


    private Drive getDriveService() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(googleAuth.getGoogleCredentialsJson())
                .createScoped(Collections.singleton(DriveScopes.DRIVE_FILE));
        return new Drive.Builder(new NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName(applicationName)
                .build();
    }

    private String uploadImageToDrive(MultipartFile file, String folderId) {
        try {
            Drive driveService = getDriveService();

            java.io.File tempFile = java.io.File.createTempFile("temp", null);
            file.transferTo(tempFile);

            File fileMetadata = new File();
            fileMetadata.setName(file.getOriginalFilename());
            fileMetadata.setParents(Collections.singletonList(folderId));

            FileContent mediaContent = new FileContent(file.getContentType(), tempFile);
            File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                    .setFields("id,webContentLink,webViewLink")
                    .execute();

            Permission permission = new Permission();
            permission.setType("anyone"); // Ai cũng xem được
            permission.setRole("reader"); // Chỉ có quyền xem

            driveService.permissions().create(uploadedFile.getId(), permission)
                    .setFields("id")
                    .execute();

            tempFile.delete();

            return uploadedFile.getWebViewLink();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String uploadImageToDrive(MultipartFile file, FolderType type) {
        return uploadImageToDrive(file, driveFolderConfig.getFolder(type));
    }

}
