package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.AddReviewRequest;
import com.pacific.pacificbe.dto.request.ReviewRequest;
import com.pacific.pacificbe.dto.request.UpdateStatusReviewRequest;
import com.pacific.pacificbe.dto.response.ReviewResponse;

import java.util.List;

public interface ReviewService {

    ReviewResponse getRatingById(String id);

    List<ReviewResponse> getAllRatings();

    void deleteRating(String id);

    ReviewResponse updateStatus(String id, UpdateStatusReviewRequest request);

    void deleteReview(String id);

    ReviewResponse createReview(ReviewRequest request);

    List<ReviewResponse> getAllReviews();

    ReviewResponse createReviewByUser(AddReviewRequest request);
}
