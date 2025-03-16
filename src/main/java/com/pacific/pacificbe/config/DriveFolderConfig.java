package com.pacific.pacificbe.config;

import com.pacific.pacificbe.utils.enums.FolderType;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "google.drive.folder")
@Getter
public class DriveFolderConfig {
    private final Map<String, String> folders = new HashMap<>();

    public String getFolder(FolderType type) {
        return folders.getOrDefault(type.name().toLowerCase(), null);
    }
}
