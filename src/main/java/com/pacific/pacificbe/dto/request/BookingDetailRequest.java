package com.pacific.pacificbe.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDetailRequest {
    String fullName;
    String email;
    String phoneNumber;
    String gender;
    Instant birthday;
    String ageGroup;
    BigDecimal price;
}
