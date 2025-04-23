package com.pacific.pacificbe.dto.response.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserVipResponse {
    String userid;
    String fullName;
    Long bookingCount;
    BigDecimal totalSpent;
}
