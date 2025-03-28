package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.response.ReviewResponse;
import com.pacific.pacificbe.dto.response.ReviewResponseBooking;
import com.pacific.pacificbe.model.Review;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewMapper {
    ModelMapper modelMapper;

    public Review toReview(ReviewResponse reviewResponse) {
        return modelMapper.map(reviewResponse, Review.class);
    }

    public List<ReviewResponse> toReviewResponseList(List<Review> reviews) {
        return reviews.stream()
                .map(this::toResponse)
                .toList();
    }

    public ReviewResponse toResponse(Review review) {
        return modelMapper.map(review, ReviewResponse.class);
    }

    public ReviewResponseBooking toReviewResponseBooking(Review review) {
        return modelMapper.map(review, ReviewResponseBooking.class);
    }

    public List<ReviewResponseBooking> toReviewResponseBookingList(List<Review> reviews) {
        return reviews.stream()
                .map(this::toReviewResponseBooking)
                .toList();
    }
}
