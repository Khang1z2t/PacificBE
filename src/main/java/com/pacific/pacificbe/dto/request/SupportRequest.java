package com.pacific.pacificbe.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportRequest {
    private String subject;
    private String message;

    @Builder.Default
    private String status = "pending";

    @Builder.Default
    private Boolean active = true;

    private String userId;
}
