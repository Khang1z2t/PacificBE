package com.pacific.pacificbe.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuideRequest {
    private String address;
    private String email;
    private Integer experienceYears;
    private String firstName;
    private String lastName;
    private String phone;
    private String active;
}