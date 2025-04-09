package com.pacific.pacificbe.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUsernameAndPassRequest {
    String username;
    String password;
    String passwordConfirm;
}
