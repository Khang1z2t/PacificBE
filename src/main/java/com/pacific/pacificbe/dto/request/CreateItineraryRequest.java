package com.pacific.pacificbe.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateItineraryRequest {
    String dayNumber;
    String title;
    String dayDetail;
    String notes;
    String tourId;
}
