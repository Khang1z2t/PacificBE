package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.dto.response.BookingRevenueReportDTO;
import com.pacific.pacificbe.dto.response.MonthlyRevenue;
import com.pacific.pacificbe.dto.response.YearlyRevenue;
import com.pacific.pacificbe.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, String>{
//  doanh thu theo tháng
    @Query(value = """
        SELECT
            MONTH(b.created_at) AS bookingMonth,
            SUM(p.total_amount) AS totalRevenue
        FROM booking b
        JOIN payment p ON b.payment_id = p.id
        GROUP BY MONTH(b.created_at)
        ORDER BY bookingMonth
        """, nativeQuery = true)
    List<MonthlyRevenue> getMonthlyRevenue();

//    Doanh thu theo năm
        @Query(value = """
        SELECT
            year(b.created_at) AS booking_year,
            SUM(p.total_amount) AS total_revenue
        FROM booking b
        JOIN payment p ON b.payment_id = p.id
        GROUP BY year(b.created_at)
        ORDER BY booking_year
        """, nativeQuery = true)
        List<YearlyRevenue> getYearlyRevenue();

//      Doanh thu tour theo thời gian ...
        @Query(value = """
        SELECT
            td.id AS tourDetailId,
            t.id AS tourId,
            t.title AS tourTitle,
            b.total_amount AS totalAmount,
            b.total_number AS totalNumber,
            FORMAT(b.created_at, 'yyyy-MM-dd') AS createdAt,
            b.user_id AS userId
        FROM tour_details td
        LEFT JOIN booking b ON td.id = b.tour_detail_id
        LEFT JOIN tour t ON t.id = td.tour_id
        WHERE (:tourId IS NULL OR td.tour_id = :tourId)
          AND (b.created_at IS NULL
          OR :createdAtStart IS NULL OR :createdAtEnd IS NULL
          OR (b.created_at BETWEEN :createdAtStart AND :createdAtEnd))
        """, nativeQuery = true)
    List<BookingRevenueReportDTO> findTourBookingsRevenue(
                @Param("tourId") String tourId,
                @Param("startDate") LocalDate startDate,
                @Param("endDate") LocalDate endDate
        );

}