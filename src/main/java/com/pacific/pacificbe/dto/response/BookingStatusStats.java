package com.pacific.pacificbe.dto.response;

import com.pacific.pacificbe.utils.enums.BookingStatus;
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
