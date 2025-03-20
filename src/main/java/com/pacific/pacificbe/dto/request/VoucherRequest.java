package com.pacific.pacificbe.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
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


    @FutureOrPresent(message = "Ngày bắt đầu phải từ hôm nay trở đi")
    private LocalDate startDate;

    @Future(message = "Ngày kết thúc phải sau ngày bắt đầu")
    private LocalDate endDate;
}