package com.pacific.pacificbe.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    List<ItineraryResponse> itineraries;
    GuideResponse guide;
    String hotelId;
    String transportId;

    Boolean active;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deleteAt;

}
