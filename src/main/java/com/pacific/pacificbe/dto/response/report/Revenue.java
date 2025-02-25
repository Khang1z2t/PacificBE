package com.pacific.pacificbe.dto.response.report;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Revenue {
    private Integer bookingDate;
    private BigDecimal totalRevenue;
}
