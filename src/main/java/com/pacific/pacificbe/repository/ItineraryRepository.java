package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.dto.response.showTour.ItineraryTourDetailResponse;
import com.pacific.pacificbe.model.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, String> {

    @Query(value = """
        SELECT
            t.id AS tourId,
            CAST(td.created_at AS DATE) AS dayTour,
            i.id AS id,
            i.active AS active,
            i.day_detail AS dayDetail,
            i.day_number AS dayNumber,
            i.notes AS notes,
            i.title AS title
        FROM tour t
        JOIN tour_details td ON t.id = td.tour_id
        JOIN itinerary i ON td.id = i.tour_detail_id
        WHERE t.id = :tourId
        AND CAST(td.created_at AS DATE) = :createdDay
        """, nativeQuery = true)
    List<ItineraryTourDetailResponse> getItineraryByTourAndDate(
            @Param("tourId") String tourId,
            @Param("createdDay") String createdDay);

    List<Itinerary> findByTourId(String id);


    @Modifying
    @Query("DELETE FROM Itinerary i WHERE i.tour.id = :tourId")
    void deleteByTourId(@Param("tourId") String tourId);
}
