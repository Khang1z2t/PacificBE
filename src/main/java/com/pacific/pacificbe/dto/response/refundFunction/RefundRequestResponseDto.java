package com.pacific.pacificbe.dto.response.refundFunction;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefundRequestResponseDto {
    private String bookingId;
    private String bookingNo;
    private String userId;
    private String userName;
    private String userEmail;
    private BigDecimal refundAmount;
    private String reason;
    private String status;
    private LocalDateTime createdAt;
}
