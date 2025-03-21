package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.dto.response.showTour.ItineraryTourDetailResponse;
import com.pacific.pacificbe.model.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, String> {

    @Query(value = """
        SELECT
            t.id AS tourId,
            FORMAT(td.created_at, 'dd') AS dayTour,
            i.id,
            i.active,
            i.day_detail,
            i.day_number,
            i.notes,
            i.title
        FROM tour t
        JOIN tour_details td ON t.id = td.tour_id
        JOIN itinerary i ON td.id = i.tour_detail_id
        WHERE t.id = :tourId
        AND FORMAT(td.created_at, 'dd') = :createdDay
        """, nativeQuery = true)
    List<ItineraryTourDetailResponse> getItineraryByTourAndDate(
            @Param("tourId") String tourId,
            @Param("createdDay") String createdDay);
}
