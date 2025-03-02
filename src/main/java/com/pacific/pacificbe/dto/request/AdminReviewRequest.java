package com.pacific.pacificbe.dto.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class AdminReviewRequest {

    private BigDecimal rating;
    private String comment;

    private String userId;
    private String bookingId;
    private String tourId;
}
