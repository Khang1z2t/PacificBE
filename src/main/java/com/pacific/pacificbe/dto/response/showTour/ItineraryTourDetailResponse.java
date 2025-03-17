package com.pacific.pacificbe.dto.response.showTour;

import com.pacific.pacificbe.dto.response.ItineraryResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItineraryTourDetailResponse {
    String tourId;
    String dayTour;
    String id;
    Boolean active;
    String dayDetail;
    Integer dayNumber;
    String notes;
    String title;
}
