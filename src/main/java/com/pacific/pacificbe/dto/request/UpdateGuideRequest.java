package com.pacific.pacificbe.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateGuideRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
    private Integer experienceYears;

}
