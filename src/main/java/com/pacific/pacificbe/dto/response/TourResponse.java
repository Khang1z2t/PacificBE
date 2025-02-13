package com.pacific.pacificbe.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourResponse {
    String id;
    String title;
    String description;
    String themeUrl;
    String capacity;
    Double basePrice;
    Double childrenPrice;
    String duration;
    String destination;
    String meetingPoint;
    String status;
    List<?> tourImages;
    List<?> guides;
}
