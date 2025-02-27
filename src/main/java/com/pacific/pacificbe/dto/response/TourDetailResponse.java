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
    String title;
    String description;
    BigDecimal priceAdults;
    BigDecimal priceChildren;
    Integer duration;
    String startDate;
    String endDate;
    Integer quantity;

    List<ItineraryResponse> itineraries;
    String comboId;
    String hotelId;
    String transportId;
    TourInDetailResponse tour;

    Boolean active;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
