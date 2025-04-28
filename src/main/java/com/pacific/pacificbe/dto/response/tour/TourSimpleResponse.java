package com.pacific.pacificbe.dto.response.tour;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourSimpleResponse {
    String id;
    String title;
    String description;
    String thumbnail;
    String slug;
}
