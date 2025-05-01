package com.pacific.pacificbe.dto.response.oauth2;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FacebookUserResponse {
    String id;
    String name;
    String email;
    String firstName;
    String lastName;
    Picture picture;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Picture {
        DataResponse data;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DataResponse {
        int height;
        int width;
        String url;
        boolean isSilhouette;
    }

}
