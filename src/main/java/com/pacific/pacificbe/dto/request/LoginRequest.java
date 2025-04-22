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
    @Schema(description = "Tên đăng nhập", example = "khang1z2t")
    @JsonProperty(value = "identifier", required = true)
    String identifier;
    @Schema(description = "Mật khẩu của tài khoản", example = "khang1z2t")
    @JsonProperty(value = "password", required = true)
    String password;
}
