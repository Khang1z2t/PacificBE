package com.pacific.pacificbe.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingRequest {
    String userId;
    String tourDetailId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate bookingDate;

    int adultNum;
    int childrenNum;
    int totalNumber;
    BigDecimal totalAmount;
    String paymentMethod;
    String specialRequests;
    String voucherId;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
