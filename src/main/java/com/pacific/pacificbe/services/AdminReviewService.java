package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.AdminReviewRequest;
import com.pacific.pacificbe.dto.response.AdminReviewResponse;

import java.util.List;

public interface AdminReviewService {

    AdminReviewResponse createReview(AdminReviewRequest request);

    AdminReviewResponse getReviewById(String id);

    List<AdminReviewResponse> getAllReviews();

    AdminReviewResponse updateReview(String id, AdminReviewRequest request);

    void deleteReview(String id);
}
