package com.pacific.pacificbe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    String id;
    BigDecimal totalAmount;
    LocalDateTime createdAt;
    String note; //base64
    String status;
    String transactionId;
}
