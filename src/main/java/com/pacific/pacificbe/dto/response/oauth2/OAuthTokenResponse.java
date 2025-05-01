package com.pacific.pacificbe.dto.response.oauth2;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OAuthTokenResponse {
    String accessToken;
    String tokenType;
    Long expiresIn;
}