package com.pacific.pacificbe.dto.response;

import com.pacific.pacificbe.model.Guide;
import com.pacific.pacificbe.model.Itinerary;
import io.swagger.v3.oas.annotations.Operation;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.awt.*;
import java.time.LocalDateTime;
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
    Double childrentPrice;
    String duration;
    String destination;
    String meetingPoint;
    String status;
    List<?> tourImages;
    String guideId;

}
