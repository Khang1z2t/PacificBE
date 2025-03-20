package com.pacific.pacificbe.dto.response.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingRevenueReportDTO {
    private String tourId;
    private String tourDetailId;
    private BigDecimal tourRevenue;
    private String bookingDate;
    private String bookingStatus;
}
