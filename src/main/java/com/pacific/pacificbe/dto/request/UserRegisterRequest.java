package com.pacific.pacificbe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRegisterRequest {
    @Schema(description = "Tên đăng nhập của tài khoản", example = "khang")
    String username;
    @Schema(description = "Mật khẩu của tài khoản", example = "12345678")
    String password;
    @Schema(description = "Họ của người dùng", example = "Bảo")
    String firstName;
    @Schema(description = "Tên của người dùng", example = "Khang")
    String lastName;
    @Schema(description = "Email của người dùng", example = "khang@example.com")
    String email;
}
