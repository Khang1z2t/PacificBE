package com.pacific.pacificbe.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AdminReviewResponse {

    private String id;
    private BigDecimal rating;
    private String comment;

    private String userId;
    private String email;
    private String bookingId;
    private String tourId;
    private String tuorName;
    private String status;
    private LocalDateTime createdAt;

}
