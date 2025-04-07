package com.pacific.pacificbe.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TopBookedUsersResponse {
    String userId;
    String firstName;
    String lastName;
    String email;
    long bookingCount;
}
