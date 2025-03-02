package com.pacific.pacificbe.dto.request;

import lombok.Data;

@Data
public class GuideRequest {
    private String address;
    private String email;
    private Integer experienceYears;
    private String firstName;
    private String lastName;
    private String phone;
}
