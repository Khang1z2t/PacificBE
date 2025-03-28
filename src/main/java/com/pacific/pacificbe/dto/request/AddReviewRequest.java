package com.pacific.pacificbe.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddReviewRequest {
    String comment;
    BigDecimal priceRating;
    BigDecimal serviceRating;
    BigDecimal facilityRating;
    BigDecimal foodRating;
    BigDecimal accommodationRating;

    String bookingId;
    String tourId;
}
