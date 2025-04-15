package com.pacific.pacificbe.dto.response.refundFunction;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionResponseDto {
    private String id;
    private String walletId;
    private String bookingNo;
    private String userId;
    private BigDecimal amount;
    private String type; // REFUND, DEPOSIT, WITHDRAW
    private String status; // PENDING, COMPLETED, FAILED
    private String createdAt;
    private String description;
}
