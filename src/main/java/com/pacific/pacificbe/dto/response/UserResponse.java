package com.pacific.pacificbe.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String firstName;
    String lastName;
    String email;
    String phone;
    String address;
    String gender;
    LocalDate birthday;
    BigDecimal deposit;
    String status;
    Boolean emailVerified;
    Boolean phoneVerified;
    String avatarUrl;
    String role;

    Boolean active;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
