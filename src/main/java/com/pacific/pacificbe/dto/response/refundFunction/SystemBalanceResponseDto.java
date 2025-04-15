package com.pacific.pacificbe.dto.response.refundFunction;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SystemBalanceResponseDto {
    private BigDecimal balance; // Số dư ví hệ thống
    private BigDecimal totalRefunded; // Tổng tiền đã hoàn
    private long totalTransactions; // Tổng số giao dịch
}
