package com.pacific.pacificbe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryAnalysis {
    private String destination;
    private BigDecimal maxPrice;
    private Integer duration;
    private Integer startMonth;
    private Integer hotelStar;

}
