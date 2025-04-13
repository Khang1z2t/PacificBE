package com.pacific.pacificbe.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupportReplyRequest {
    private String responseMessage; // Admin phản hồi
    private String subject;
}

