package com.pacific.pacificbe.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingResponse {
    String id;
    String bookingNo;
    Integer adultNum;
    Integer childrenNum;
    Integer totalNumber;
    BigDecimal totalAmount;
    String paymentMethod;
    String specialRequests;
    String tourDetailId;
    String status;
    String voucherId;
    String userId;

    List<BookingDetailResponse> details;

    PaymentResponse payment;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deleteAt;
}
