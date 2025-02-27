package com.pacific.pacificbe.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourInDetailResponse {
    String id;
    String title;
    String description;
    String status;
    String thumbnail;
    List<?> images;

    String category;
    GuideResponse guide;
}
