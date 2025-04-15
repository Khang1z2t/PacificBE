package com.pacific.pacificbe.dto.response.refundFunction;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BalanceResponseDto {
    private String id; // userId hoặc walletId
    private BigDecimal balance; // Số dư deposit hoặc system_wallet
}
