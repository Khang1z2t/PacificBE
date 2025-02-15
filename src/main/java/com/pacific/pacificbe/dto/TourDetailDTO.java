package com.pacific.pacificbe.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourDetailDTO {
    String id;
    String itinerary;
    String startDate;
    String endDate;
    Integer duration;
}
