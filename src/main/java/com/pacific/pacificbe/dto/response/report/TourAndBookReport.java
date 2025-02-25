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
public class TourAndBookReport {
    private String bookingId;
    private String tourId;
    private String tourDetailId;
    private String userName;
    private String bookingStatus;
    private BigDecimal totalAmount;
    private Integer totalNumber;
    private String paymentMethod;
    private String createdAt;
}
