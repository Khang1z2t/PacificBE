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
    BigDecimal priceAdults;
    BigDecimal priceChildren;
    Double ratingAvg;
    String status;
    List<?> images;

    String category;
    GuideResponse guide;

    Boolean active;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
