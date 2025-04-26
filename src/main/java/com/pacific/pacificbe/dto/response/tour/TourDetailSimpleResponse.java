package com.pacific.pacificbe.dto.response.tour;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourDetailSimpleResponse {
    String id;
    BigDecimal priceAdults;
    BigDecimal priceChildren;
    LocalDateTime startDate;
    LocalDateTime endDate;
    Integer quantity;
    String status;
    TourSimpleResponse tour;
}
