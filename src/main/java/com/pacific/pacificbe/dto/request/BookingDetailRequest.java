package com.pacific.pacificbe.dto.request;

import com.pacific.pacificbe.utils.enums.AgeGroup;
import com.pacific.pacificbe.utils.enums.GenderEnums;
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
    GenderEnums gender;
    Instant birthday;
    AgeGroup ageGroup;
    BigDecimal price;
}
