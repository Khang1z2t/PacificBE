package com.pacific.pacificbe.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class UpdateUserRequest {
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String address;
    private String gender;
    private LocalDate birthday;
    private String status;
    private BigDecimal deposit;
    private String avatar;
    private String role;
}
