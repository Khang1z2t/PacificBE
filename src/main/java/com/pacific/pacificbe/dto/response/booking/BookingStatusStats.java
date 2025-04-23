package com.pacific.pacificbe.dto.response.booking;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingStatusStats {
    String status;
    Long count;
    double percentage;
}
