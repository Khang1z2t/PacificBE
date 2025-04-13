package com.pacific.pacificbe.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupportRequest {
    private String name;
    private String email;
    private String subject;
    private String message;

    @Builder.Default
    private String status = "pending";

    private String userEmail;

    private LocalDateTime createdAt;
}
