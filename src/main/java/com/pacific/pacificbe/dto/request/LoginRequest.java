package com.pacific.pacificbe.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {
    @Schema(description = "Tên đăng nhập", example = "khang")
    @JsonProperty(value = "username", required = true)
    String username;
    @Schema(description = "Mật khẩu của tài khoản", example = "12345678")
    @JsonProperty(value = "password", required = true)
    String password;
}
