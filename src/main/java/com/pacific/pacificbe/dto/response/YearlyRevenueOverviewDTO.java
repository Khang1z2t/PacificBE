package com.pacific.pacificbe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class YearlyRevenueOverviewDTO {
    private BigDecimal totalRevenue; // Tổng doanh thu năm nay
    private BigDecimal revenueChange; // Chênh lệch doanh thu so với năm ngoái
    private ChangeDTO change; // Tỷ lệ tăng/giảm
    private List<MonthlyRevenueDTO> monthlyRevenues; // Doanh thu từng tháng
}