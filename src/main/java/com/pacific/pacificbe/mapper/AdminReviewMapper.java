package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.request.AdminReviewRequest;
import com.pacific.pacificbe.dto.response.AdminReviewResponse;
import com.pacific.pacificbe.model.Review;
import org.springframework.stereotype.Component;

@Component
public class AdminReviewMapper {

    public AdminReviewResponse toResponse(Review review) {
        if (review == null) {
            return null;
        }
        AdminReviewResponse response = new AdminReviewResponse();
        response.setId(review.getId());
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setUserId(review.getUser().getId());
        response.setBookingId(review.getBooking().getId());
        response.setTourId(review.getTour().getId());
        return response;
    }

    public void updateEntityFromRequest(AdminReviewRequest request, Review review) {
        if (request.getRating() != null) {
            review.setRating(request.getRating());
        }
        review.setComment(request.getComment());

    }
}
