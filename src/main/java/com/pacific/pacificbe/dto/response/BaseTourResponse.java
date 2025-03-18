package com.pacific.pacificbe.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseTourResponse {
    String id;
    String title;
    String description;
    Integer duration;
    String status;
    String thumbnail;
    List<?> images;

    Double ratingAvg;
    BigDecimal maxPrice;
    BigDecimal minPrice;

    String destination;
    String category;
    GuideResponse guide;

    Boolean active;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deleteAt;
}
