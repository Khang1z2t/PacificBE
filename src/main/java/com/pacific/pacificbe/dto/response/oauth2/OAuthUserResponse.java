package com.pacific.pacificbe.dto.response.oauth2;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.info.Contact;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OAuthUserResponse {
    private String email;
    private String firstName;
    private String lastName;
    private String picture;
    private String sub; // Dành cho Google
    private String id;  // Dành cho Facebook
    private String name;

}
