package com.pacific.pacificbe.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuideResponse {
    private String id;
    private String address;
    private String email;
    private Integer experience_years;
    private String first_name;
    private String last_name;
    private String phone;
}
