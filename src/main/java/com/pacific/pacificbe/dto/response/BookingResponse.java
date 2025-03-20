package com.pacific.pacificbe.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingResponse {
    String bookingId;
    String userId;
    String userName;
    String tourDetailId;
    String tourName;
    LocalDateTime bookingDate;
    int adultNum;
    int childrenNum;
    int totalNumber;
    BigDecimal totalAmount;
    String paymentMethod;
    String bookingStatus;
    String specialRequests;
    String voucherId;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}