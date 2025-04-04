package com.pacific.pacificbe.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingLevel {
    int ratingLevel;
    long count;
    double percentage;
}
