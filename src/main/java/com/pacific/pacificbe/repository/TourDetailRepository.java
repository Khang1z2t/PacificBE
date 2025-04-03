package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.dto.response.report.Revenue;
import com.pacific.pacificbe.dto.response.showTour.DetailTourResponse;
import com.pacific.pacificbe.model.Booking;
import com.pacific.pacificbe.model.TourDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TourDetailRepository extends JpaRepository<TourDetail, String> {
    List<TourDetail> findByTourId(String tourId);

    void deleteByTourId(String tourId);

    //Tìm tháng tour theo id tour
    @Query(value = """
    SELECT
        FORMAT(td.start_date, 'yyyy-MM') AS created_month,
        td.tour_id
    FROM tour_details td
    WHERE td.tour_id = :tourId OR LOWER(td.tour_id) LIKE LOWER(CONCAT('%', :tourId, '%'))
    AND td.status = 'ACTIVE'
    GROUP BY td.tour_id, FORMAT(td.start_date, 'yyyy-MM'),td.status
    """, nativeQuery = true)
    List<DetailTourResponse> getTourDetailMonth(
            String tourId
    );

    //Tìm ngày tour theo id tour
    @Query(value = """
    SELECT
        FORMAT(td.start_date, 'dd') AS createdDay,
        td.id
    FROM tour_details td
    WHERE td.tour_id = :tourId
    AND FORMAT(td.start_date, 'yyyy-MM') = :months
    AND td.status = 'ACTIVE'
    """, nativeQuery = true)
    List<DetailTourResponse> getTourDetailDay(
            String tourId,
            String months
    );

    TourDetail findByBookings(Booking bookings);
}
