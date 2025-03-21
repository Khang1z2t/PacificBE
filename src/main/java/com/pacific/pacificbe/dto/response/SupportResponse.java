package com.pacific.pacificbe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupportResponse {
    private String id;
    private String subject;
    private String message;
    private String status;
    private String username;
    private String email;
}
