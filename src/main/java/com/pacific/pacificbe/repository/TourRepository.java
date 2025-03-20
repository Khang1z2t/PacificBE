package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.dto.response.showTour.ItineraryTourDetailResponse;
import com.pacific.pacificbe.dto.response.showTour.TourBookingCount;
import com.pacific.pacificbe.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<Tour, String> {

    @Query("SELECT t FROM Tour t " +
            "LEFT JOIN t.tourDetails td " +
            "WHERE (:title IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:minPrice IS NULL OR td.priceAdults >= :minPrice) " +
            "AND (:maxPrice IS NULL OR td.priceAdults <= :maxPrice) " +
            "AND (:categoryId IS NULL OR t.category.id = :categoryId)")
    List<Tour> findAllWithFilters(@Param("title") String title,
                                  @Param("minPrice") BigDecimal minPrice,
                                  @Param("maxPrice") BigDecimal maxPrice,
                                  @Param("categoryId") String categoryId);

    List<Tour> findToursByActiveIsTrue();

    @Query(value = """
            select
            	COUNT(t.id) as bookingCount,
            	td.id as detailID,
            	t.id as tourID
            from tour t
            	join tour_details td on t.id = td.tour_id
            	join booking b on td.id = b.tour_detail_id
            	join users us on us.id = b.user_id
            where :tourId IS NULL OR t.id = :tourId
            	group by t.id, td.id
            	order by t.id asc
            """, nativeQuery = true)
    List<TourBookingCount> findTourBookingCounts(@Param("tourId") String tourId);

    @Procedure(procedureName = "FineTourCategory")
    List<Tour> findTourCategory(@Param("category") String category);
    
    @Procedure(procedureName = "FindTourRating")
    List<Tour> findTourRating(@Param("rating") Double rating);
    
    @Procedure(procedureName = "FindTourDestination")
    List<Tour> findTourDestination(@Param("destination") String destination);
}
