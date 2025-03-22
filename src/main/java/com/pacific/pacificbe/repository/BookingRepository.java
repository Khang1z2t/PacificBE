package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.dto.response.report.BookingRevenueReportDTO;
import com.pacific.pacificbe.dto.response.report.Revenue;
import com.pacific.pacificbe.dto.response.report.TourAndBookReport;
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
            SUM(b.total_amount) AS totalRevenue
        FROM booking b
        WHERE YEAR(b.created_at) = :years
            AND (:bookingStatus IS NULL OR LOWER(b.booking_status) LIKE LOWER(CONCAT('%', :bookingStatus, '%')))
            GROUP BY MONTH(b.created_at), YEAR(b.created_at)
            ORDER BY bookingMonth
        """, nativeQuery = true)
    List<Revenue> getMonthlyRevenue(
            @Param("years") String years,
            @Param("bookingStatus") String bookingStatus
    );

    //    Doanh thu theo năm
    @Query(value = """
        SELECT
            year(b.created_at) AS booking_year,
            SUM(b.total_amount) AS total_revenue
        FROM booking b
        WHERE (:bookingStatus IS NULL OR LOWER(b.booking_status) LIKE LOWER(CONCAT('%', :bookingStatus, '%')))
            GROUP BY year(b.created_at)
            ORDER BY booking_year
        """, nativeQuery = true)
    List<Revenue> getYearlyRevenue(
            @Param("bookingStatus") String bookingStatus
    );

    //      Doanh thu tour theo thời gian ...
    @Query(value = """
            SELECT
                t.id AS tour_id,
                td.id AS tour_detail_id,
                SUM(b.total_amount) AS tourRevenue,
                FORMAT(b.created_at, 'dd/MM/yyyy') AS booking_date,
                b.booking_status
            FROM tour t
                JOIN tour_details td ON t.id = td.tour_id
                JOIN booking b ON td.id = b.tour_detail_id
            WHERE
                (:tourId IS NULL OR t.id = :tourId)
                AND (:bookingStatus IS NULL OR b.booking_status = :bookingStatus)
                AND (:startDate IS NULL OR :endDate IS NULL
                OR b.created_at BETWEEN :startDate AND :endDate)
            GROUP BY
                td.id, t.id, FORMAT(b.created_at, 'dd/MM/yyyy'), b.booking_status
            ORDER BY
                booking_date ASC
        """, nativeQuery = true)
    List<BookingRevenueReportDTO> getTourBookingsRevenue(
            @Param("tourId") String tourId,
            @Param("bookingStatus") String bookingStatus,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    //      Doanh thu theo từng tour và từng khách hàng
    @Query(value = """
        SELECT
            b.id,
            t.id as tour_id,
            td.id as tour_detail_id,
            us.username,
            b.booking_status,
            b.total_amount,
            b.total_number,
            b.payment_method,
            FORMAT(b.created_at, 'yyyy-MM-dd') as created_at
        FROM booking b
            JOIN users us ON us.id = b.user_id
            JOIN tour_details td ON td.id = b.tour_detail_id
            JOIN tour t ON t.id = td.tour_id
                WHERE (:tourId IS NULL OR t.id = :tourId)
                AND (:username IS NULL OR LOWER(us.username) LIKE LOWER(CONCAT('%', :username, '%')))
        """, nativeQuery = true)
    List<TourAndBookReport> getTourAndBooking(
            @Param("tourId") String tourId,
            @Param("username") String username
    );
}