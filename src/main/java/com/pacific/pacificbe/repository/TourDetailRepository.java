package com.pacific.pacificbe.repository;


import com.pacific.pacificbe.dto.response.showTour.DetailTourResponse;
import com.pacific.pacificbe.model.TourDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface TourDetailRepository extends JpaRepository<TourDetail, String> {
    List<TourDetail> findByTourId(String tourId);

    void deleteByTourId(String tourId);

    //Tìm tháng tour theo id tour
    @Query(value = """
            SELECT
                    createdMonth,
                    tour_id,
                    STRING_AGG(status, ',') AS status
                FROM (
                    SELECT DISTINCT
                        FORMAT(td.start_date, 'yyyy-MM') AS createdMonth,
                        td.tour_id,
                        td.status
                    FROM tour_details td
                    WHERE (td.tour_id = :tourId
                        OR LOWER(td.tour_id) LIKE LOWER(CONCAT('%', :tourId, '%')))
                        AND td.status != 'CLOSED'
                ) AS distinct_statuses
                GROUP BY createdMonth, tour_id
            """, nativeQuery = true)
    List<DetailTourResponse> getTourDetailMonth(
            String tourId
    );

    //Tìm ngày tour theo id tour
    @Query(value = """
            SELECT
                FORMAT(td.start_date, 'dd') AS createdDay,
                td.id,
                td.status
            FROM tour_details td
            WHERE td.tour_id = :tourId
            AND FORMAT(td.start_date, 'yyyy-MM') = :months
            """, nativeQuery = true)
    List<DetailTourResponse> getTourDetailDay(
            String tourId,
            String months
    );

    // Đếm tổng số tour_details (có thể lọc theo thời gian)
    @Query("SELECT COUNT(td) FROM TourDetail td WHERE " +
            "(:startDate IS NULL OR td.startDate >= :startDate) AND " +
            "(:endDate IS NULL OR td.endDate <= :endDate)")
    long countTourDetails(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Đếm số lượng tour_details theo khoảng rating_avg
    @Query("SELECT COUNT(td) FROM TourDetail td WHERE " +
            "td.ratingAvg >= :minRating AND td.ratingAvg <= :maxRating AND " +
            "(:startDate IS NULL OR td.startDate >= :startDate) AND " +
            "(:endDate IS NULL OR td.endDate <= :endDate)")
    long countByRatingRange(
            @Param("minRating") float minRating,
            @Param("maxRating") float maxRating,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT td FROM TourDetail td WHERE " +
            "(td.status = 'OPEN' AND td.startDate < :now) OR " +
            "(td.status = 'IN_PROGRESS' AND td.endDate < :now) OR " +
            "(td.status = 'CLOSED' AND td.startDate >= :now AND td.endDate >= :now) OR " +
            "(td.status = 'CANCELED' AND td.startDate >= :now AND td.endDate >= :now)")
    Page<TourDetail> findTourDetailsToUpdateStatus(@Param("now") LocalDateTime now, Pageable pageable);

    @Modifying
    @Query("UPDATE TourDetail td SET td.status = 'IN_PROGRESS' " +
            "WHERE td.status = 'OPEN' AND td.startDate < :now AND td.endDate > :now")
    int updateOpenToInProgress(@Param("now") LocalDateTime now);

    @Modifying
    @Query("UPDATE TourDetail td SET td.status = 'CLOSED' " +
            "WHERE td.status = 'IN_PROGRESS' AND td.endDate < :now")
    int updateInProgressToClosed(@Param("now") LocalDateTime now);

    @Modifying
    @Query("UPDATE TourDetail td SET td.status = 'CLOSED' " +
            "WHERE td.status = 'OPEN' AND td.endDate < :now")
    int updateOpenToClosed(@Param("now") LocalDateTime now);

    @Modifying
    @Query("UPDATE TourDetail td SET td.status = 'OPEN' " +
            "WHERE td.status = 'CLOSED' AND td.startDate >= :now AND td.endDate >= :now")
    int updateClosedToOpen(@Param("now") LocalDateTime now);

    @Modifying
    @Query("UPDATE TourDetail td SET td.status = 'OPEN' " +
            "WHERE td.status = 'CANCELED' AND td.startDate >= :now AND td.endDate >= :now")
    int updateCanceledToOpen(@Param("now") LocalDateTime now);

    @Query("select t from TourDetail t where t.status = ?1 and t.startDate < ?2")
    List<TourDetail> findByStatusAndStartDateBefore(String status, LocalDateTime startDate);

    @Query("select t from TourDetail t where t.status = ?1 and t.endDate < ?2")
    List<TourDetail> findByStatusAndEndDateBefore(String status, LocalDateTime endDate);

//    TEST AI
    List<TourDetail> findByTourIdAndActiveTrue(String tourId);
    List<TourDetail> findByPriceAdultsLessThanEqual(BigDecimal price);
}
