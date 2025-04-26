package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.dto.response.showTour.TourBookingCount;
import com.pacific.pacificbe.dto.response.showTour.TourDateResponse;
import com.pacific.pacificbe.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TourRepository extends JpaRepository<Tour, String> {

    //    @Query(value = """
//            SELECT t.*
//            FROM tour t
//            JOIN tour_details td ON t.id = td.tour_id
//            WHERE (:title IS NULL OR TRIM(LOWER(t.title)) LIKE CONCAT('%', TRIM(LOWER(:title)), '%'))
//              AND (:minPrice IS NULL OR td.price_adults >= :minPrice)
//              AND (:maxPrice IS NULL OR td.price_adults <= :maxPrice)
//              AND (:startDate IS NULL OR td.start_date >= :startDate)
//              AND (:endDate IS NULL OR td.end_date <= :endDate)
//              AND (:categoryId IS NULL OR t.category_id = :categoryId)
//              AND t.active = 1
//            ORDER BY td.price_adults DESC
//            """, nativeQuery = true)
    @Query(value = """
            SELECT DISTINCT t.*
            FROM tour t
            LEFT JOIN (
                SELECT tour_id, MIN(price_adults) AS min_price, MAX(price_adults) AS max_price
                FROM tour_details
                WHERE active = 1
                  AND (:startDate IS NULL OR :endDate IS NULL OR start_date BETWEEN :startDate AND :endDate)
                GROUP BY tour_id
            ) price_summary ON price_summary.tour_id = t.id
            LEFT JOIN destination d ON d.id = t.destination_id
            WHERE (:title IS NULL OR LOWER(TRIM(t.title)) LIKE CONCAT('%', LOWER(TRIM(:title)), '%'))
              AND (:categoryId IS NULL OR t.category_id = :categoryId)
              AND (:minPrice IS NULL OR price_summary.min_price >= :minPrice OR price_summary.min_price IS NULL)
              AND (:maxPrice IS NULL OR price_summary.max_price <= :maxPrice OR price_summary.max_price IS NULL)
              AND (:region IS NULL OR LOWER(TRIM(d.region)) LIKE CONCAT('%', LOWER(TRIM(:region)), '%'))
              AND t.active = 1
            """, nativeQuery = true)
    List<Tour> findAllWithFilters(
            @Param("title") String title,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("categoryId") String categoryId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("region") String region);

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

    Optional<Tour> findByTourDetails_Id(String id);

    @Query(value = "SELECT * FROM tour t WHERE t.active = 1 AND t.status = :status", nativeQuery = true)
    List<Tour> findAllTour(@Param("status") String status);


    //    TEST AI
    List<Tour> findByActiveTrueAndStatus(String status);

    List<Tour> findByDestinationCityContainingIgnoreCase(String city);

    @Query("select t from Tour t left join t.tourDetails tourDetails where tourDetails.id in ?1")
    List<Tour> findByTourDetails_IdIn(Collection<String> ids);

}
