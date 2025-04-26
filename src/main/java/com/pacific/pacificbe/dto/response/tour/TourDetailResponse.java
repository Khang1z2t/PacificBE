package com.pacific.pacificbe.dto.response.tour;

import com.pacific.pacificbe.dto.response.GuideResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourDetailResponse {
    String id;
    BigDecimal priceAdults;
    BigDecimal priceChildren;
    LocalDateTime startDate;
    LocalDateTime endDate;
    Integer quantity;
    Double ratingAvg;
    Integer duration;

    GuideResponse guide;
    String hotelId;
    String transportId;

    String status;

    Boolean active;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deleteAt;

}
