package com.pacific.pacificbe.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherRequest {
    private String nameVoucher;
    private String codeVoucher;
    private BigDecimal discount;
    private Integer quantity;

    @Builder.Default
    private String status = "pending";

    @Builder.Default
    private Boolean active = true;

    @Builder.Default
    private LocalDate startDate = LocalDate.now();

    @Builder.Default
    private LocalDate endDate = LocalDate.now();
}