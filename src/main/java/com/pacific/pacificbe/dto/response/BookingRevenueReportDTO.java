package com.pacific.pacificbe.dto.response;

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
    private String tourDetailId;
    private String tourId;
    private String tourTitle;
    private BigDecimal totalAmount;
    private Integer totalNumber;
    private String createdAt;
    private String  userId;
}
