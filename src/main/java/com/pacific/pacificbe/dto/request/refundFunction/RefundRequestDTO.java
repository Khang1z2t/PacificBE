package com.pacific.pacificbe.dto.request.refundFunction;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefundRequestDTO {
    String bookingId;
    String reasons;
}
