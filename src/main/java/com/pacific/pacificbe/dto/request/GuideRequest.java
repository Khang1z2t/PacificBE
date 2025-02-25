package com.pacific.pacificbe.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuideRequest {
    private String address;
    private String email;
    private Integer experienceYears;
    private String first_name;
    private String last_name;
    private String phone;
}
