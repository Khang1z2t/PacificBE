package com.pacific.pacificbe.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuideResponse {
    private String id;
    private String address;
    private String email;
    private Integer experienceYears;
    private String firstName;
    private String lastName;
    private String phone;
}
