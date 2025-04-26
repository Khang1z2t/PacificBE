package com.pacific.pacificbe.dto.response.tour;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourDestinationResponse {
    String id;
    String country;
}
