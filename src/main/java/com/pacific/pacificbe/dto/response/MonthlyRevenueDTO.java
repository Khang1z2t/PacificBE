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
public class MonthlyRevenueDTO {
    private Integer month; // Tháng (1-12)
    private BigDecimal revenue; // Doanh thu
}