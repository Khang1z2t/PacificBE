package com.pacific.pacificbe.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherRequest {
    private String title;
    private String codeVoucher;
    private BigDecimal discountValue;
    private Integer quantity;
    private Integer userLimit;
    private BigDecimal minOrderValue;
    private BigDecimal maxDiscountAmount;
    private Boolean firstTimeUserOnly;
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private String applyTo;
    private String tourId;
    private String categoryId;
}