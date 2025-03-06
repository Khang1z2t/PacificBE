package com.pacific.pacificbe.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
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
    String status;
    String thumbnail;
    List<?> images;

    Integer avgDuration;
    BigDecimal maxPrice;
    BigDecimal minPrice;

    String category;
    GuideResponse guide;

    Boolean active;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
