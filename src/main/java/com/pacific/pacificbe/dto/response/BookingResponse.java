package com.pacific.pacificbe.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingResponse {
    String id;
    Integer adultNum;
    Integer childrenNum;
    Integer totalNumber;
    String paymentMethod;
    String specialRequests;
    String tourDetailId;
    String voucherId;
    String status;
    String userId;

    List<BookingDetailResponse> details;

    PaymentResponse payment;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deleteAt;
}
