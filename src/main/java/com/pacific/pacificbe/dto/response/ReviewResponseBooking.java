package com.pacific.pacificbe.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResponseBooking {
    String id;
    String comment;
    BigDecimal priceRating;
    BigDecimal serviceRating;
    BigDecimal facilityRating;
    BigDecimal foodRating;
    BigDecimal accommodationRating;
    BigDecimal rating;
    LocalDateTime createdAt;
}
