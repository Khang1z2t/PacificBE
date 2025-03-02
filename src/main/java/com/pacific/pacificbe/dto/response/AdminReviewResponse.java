package com.pacific.pacificbe.dto.response;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class AdminReviewResponse {

    private String id;
    private BigDecimal rating;
    private String comment;

    private String userId;
    private String bookingId;
    private String tourId;
}
