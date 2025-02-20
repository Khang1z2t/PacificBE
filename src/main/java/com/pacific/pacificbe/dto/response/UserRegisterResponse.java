package com.pacific.pacificbe.dto.response;

import com.pacific.pacificbe.utils.enums.UserRole;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRegisterResponse {
    String id;
    String username;
    String firstName;
    String lastName;
    String email;
    String role;
    LocalDateTime createdAt;
    String accessToken;
}
