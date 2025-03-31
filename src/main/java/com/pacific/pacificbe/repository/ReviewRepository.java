package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.dto.response.ReviewResponse;
import com.pacific.pacificbe.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String>{
    void delete(Review review);
    List<Review> findByTourIdAndStatus(String tourId, String status);

    @Query("SELECT new com.pacific.pacificbe.dto.response.ReviewResponse( " +
            "r.id, r.status, r.comment, r.rating, u.email, t.title, r.createdAt) " + // Giữ nguyên u.email
            "FROM Review r " +
            "JOIN r.user u " +
            "JOIN r.tour t")
    List<ReviewResponse> getAllReviewsWithDetails();
    List<Review> findByTourId(String tourId);

    Long countByTourIdAndStatus(String tourId, String status);
}