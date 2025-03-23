package com.pacific.pacificbe.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminReviewRequest {
    private BigDecimal rating;
    private String comment;
    private String userId;
    private String bookingId;
    private String tourId;
    private String status;
    private String tourName;

    @Builder.Default
    private Boolean active = true;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
