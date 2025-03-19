package com.pacific.pacificbe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoucherResponse {
    private String id;
    private String nameVoucher;
    private String codeVoucher;
    private BigDecimal discount;
    private Integer quantity;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
}
