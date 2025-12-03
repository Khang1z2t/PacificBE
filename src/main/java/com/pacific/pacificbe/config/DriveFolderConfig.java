package com.pacific.pacificbe.config;

import com.pacific.pacificbe.utils.enums.FolderType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "google.drive")
@Getter
@Setter
public class DriveFolderConfig {
//    private String root;
//    private String avatar;
//    private String tour;
//    private String tourDetail; // Spring sẽ tự động map tour_detail thành tourDetail
//    private String resources;
//    private String hotel;
//    private String transport;

    private Map<String, String> folder = new HashMap<>();


    public String getFolder(FolderType type) {
        return folder.getOrDefault(type.name().toLowerCase(), null);
    }

}
