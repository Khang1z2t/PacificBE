package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.dto.response.showTour.TourBookingCount;
import com.pacific.pacificbe.dto.response.showTour.TourDateResponse;
import com.pacific.pacificbe.model.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TourRepository extends JpaRepository<Tour, String> {

    @Query("SELECT t FROM Tour t " +
            "LEFT JOIN t.tourDetails td " +
            "WHERE (:title IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:minPrice IS NULL OR td.priceAdults >= :minPrice) " +
            "AND (:maxPrice IS NULL OR td.priceAdults <= :maxPrice) " +
            "AND (:categoryId IS NULL OR t.category.id = :categoryId)" +
            "AND (:startDate IS NULL OR :endDate IS NULL OR (td.startDate BETWEEN :startDate AND :endDate))")
    List<Tour> findAllWithFilters(@Param("title") String title,
                                  @Param("minPrice") BigDecimal minPrice,
                                  @Param("maxPrice") BigDecimal maxPrice,
                                  @Param("categoryId") String categoryId,
                                  @Param("startDate") LocalDate startDate,
                                  @Param("endDate") LocalDate endDate);


    List<Tour> findToursByActiveIsTrue();

    @Query(value = """
            SELECT
                COUNT(t.id) AS bookingCount,
                td.id AS tourDetailId,
                t.id AS tourID,
                t.title AS tourTitle,
                td.start_date AS startDate,
                td.end_date AS endDate,
                b.total_amount AS totalAmount,
                b.booking_no AS bookingNo
            FROM tour t
                JOIN tour_details td ON t.id = td.tour_id
                JOIN booking b ON td.id = b.tour_detail_id
                JOIN users us ON us.id = b.user_id
            WHERE :tourId IS NULL OR t.id = :tourId
            GROUP BY td.id, t.id, t.title, td.start_date, td.end_date, b.total_amount, b.booking_no
            ORDER BY t.id ASC
            """, nativeQuery = true)
    List<TourBookingCount> findTourBookingCounts(@Param("tourId") String tourId);


    @Query(value = """
            select
                t.id,
                t.title,
                t.description,
                t.duration,
                t.status,
                t.thumbnail_url,
                t.available,
                t.category_id,
                t.destination_id,
                t.active,
                t.created_at,
                t.updated_at,
                t.delete_at
            from tour t
                join tour_details td on t.id = td.tour_id
            where
                td.start_date is null or td.start_date between :startDate and :endDate
            """, nativeQuery = true)
    List<TourDateResponse> findToursByDate(@Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

    Optional<Tour> findByTourDetails_Id(String id);


}
