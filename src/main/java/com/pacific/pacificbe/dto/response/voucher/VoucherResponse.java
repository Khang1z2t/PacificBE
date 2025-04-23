package com.pacific.pacificbe.dto.response.voucher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoucherResponse {
    private String id;
    private String title;
    private String codeVoucher;
    private BigDecimal discountValue;
    private Integer quantity;
    private Integer userLimit;
    private BigDecimal minOrderValue;
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private String applyTo;
    private String tourId;
    private String categoryId;

    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deleteAt;

}
