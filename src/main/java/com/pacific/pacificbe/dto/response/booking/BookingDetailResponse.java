package com.pacific.pacificbe.dto.response.booking;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDetailResponse {
    String id;
    String fullName;
    String email;
    String phoneNumber;
    Instant birthday;
    String ageGroup;
    BigDecimal price;

//    LocalDateTime createdAt;
//    LocalDateTime updatedAt;
//    LocalDateTime deleteAt;
}
