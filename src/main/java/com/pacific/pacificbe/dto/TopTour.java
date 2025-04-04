package com.pacific.pacificbe.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TopTour {
    String tourId;
    String tourTitle;
    long bookingCount;
}
