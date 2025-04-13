package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.dto.response.ReviewResponse;
import com.pacific.pacificbe.model.Review;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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


    // Đếm tổng số review (có thể lọc theo thời gian)
    @Query("SELECT COUNT(r) FROM Review r WHERE " +
            "(:startDate IS NULL OR r.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR r.createdAt <= :endDate)")
    long countReviews(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Đếm số lượng review theo khoảng accommodation_rating
    @Query("SELECT COUNT(r) FROM Review r WHERE " +
            "r.accommodationRating >= :minRating AND r.accommodationRating <= :maxRating AND " +
            "(:startDate IS NULL OR r.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR r.createdAt <= :endDate)")
    long countByAccommodationRatingRange(
            @Param("minRating") float minRating,
            @Param("maxRating") float maxRating,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Đếm số lượng review theo khoảng facility_rating
    @Query("SELECT COUNT(r) FROM Review r WHERE " +
            "r.facilityRating >= :minRating AND r.facilityRating <= :maxRating AND " +
            "(:startDate IS NULL OR r.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR r.createdAt <= :endDate)")
    long countByFacilityRatingRange(
            @Param("minRating") float minRating,
            @Param("maxRating") float maxRating,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Đếm số lượng review theo khoảng food_rating
    @Query("SELECT COUNT(r) FROM Review r WHERE " +
            "r.foodRating >= :minRating AND r.foodRating <= :maxRating AND " +
            "(:startDate IS NULL OR r.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR r.createdAt <= :endDate)")
    long countByFoodRatingRange(
            @Param("minRating") float minRating,
            @Param("maxRating") float maxRating,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Đếm số lượng review theo khoảng price_rating
    @Query("SELECT COUNT(r) FROM Review r WHERE " +
            "r.priceRating >= :minRating AND r.priceRating <= :maxRating AND " +
            "(:startDate IS NULL OR r.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR r.createdAt <= :endDate)")
    long countByPriceRatingRange(
            @Param("minRating") float minRating,
            @Param("maxRating") float maxRating,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Đếm số lượng review theo khoảng service_rating
    @Query("SELECT COUNT(r) FROM Review r WHERE " +
            "r.serviceRating >= :minRating AND r.serviceRating <= :maxRating AND " +
            "(:startDate IS NULL OR r.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR r.createdAt <= :endDate)")
    long countByServiceRatingRange(
            @Param("minRating") float minRating,
            @Param("maxRating") float maxRating,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT r FROM Review r " +
            "WHERE r.booking.tourDetail.id = :tourDetailId " +
            "AND r.booking.status = :status")
    List<Review> findByTourDetailIdAndBookingStatus(@Param("tourDetailId") String tourDetailId,
                                                    @Param("status") String status);
}