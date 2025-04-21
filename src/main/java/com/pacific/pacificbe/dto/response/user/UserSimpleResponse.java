package com.pacific.pacificbe.dto.response.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSimpleResponse {
    String id;
    String username;
    String firstName;
    String lastName;
    String email;
}
