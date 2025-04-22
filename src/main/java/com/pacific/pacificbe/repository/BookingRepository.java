package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.dto.TopTour;
import com.pacific.pacificbe.dto.response.MonthlyRevenueDTO;
import com.pacific.pacificbe.dto.response.report.BookingRevenueReportDTO;
import com.pacific.pacificbe.dto.response.report.Revenue;
import com.pacific.pacificbe.dto.response.report.TourAndBookReport;
import com.pacific.pacificbe.model.Booking;
import com.pacific.pacificbe.model.User;
import com.pacific.pacificbe.utils.enums.BookingStatus;
import com.pacific.pacificbe.utils.enums.TourDetailStatus;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, String> {
    //  doanh thu theo tháng
    @Query(value = """
            SELECT
                MONTH(b.created_at) AS bookingMonth,
                SUM(b.total_amount) AS totalRevenue
            FROM booking b
            WHERE YEAR(b.created_at) = :years
                AND (:bookingStatus IS NULL OR LOWER(b.status) LIKE LOWER(CONCAT('%', :bookingStatus, '%')))
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
            WHERE (:bookingStatus IS NULL OR LOWER(b.status) LIKE LOWER(CONCAT('%', :bookingStatus, '%')))
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
                                t.title AS tourTitle,
                                td.id AS tour_detail_id,
                                SUM(b.total_amount) AS tourRevenue,
                                FORMAT(b.created_at, 'dd/MM/yyyy') AS booking_date,
                                b.status AS bookingStatus
                            FROM tour t
                                JOIN tour_details td ON t.id = td.tour_id
                                JOIN booking b ON td.id = b.tour_detail_id
                            WHERE
                                (:tourId IS NULL OR t.id = :tourId)
                                AND (:bookingStatus IS NULL OR b.status = :bookingStatus)
            AND (:startDate IS NULL OR :endDate IS NULL OR b.created_at BETWEEN :startDate AND :endDate)
                            GROUP BY
                                td.id, t.id, t.title, FORMAT(b.created_at, 'dd/MM/yyyy'), b.status
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

    @Query(value = "SELECT TOP 1 b.booking_no FROM booking b WHERE b.booking_no LIKE CONCAT('B', FORMAT(GETDATE(), 'ddMMyy'), '%') ORDER BY CAST(SUBSTRING(b.booking_no, 13, 4) AS INTEGER) DESC", nativeQuery = true)
    String findLatestBookingNoOfToday();

    long countByUserIdAndVoucherId(String userId, String voucherId);

    Optional<Booking> findByBookingNo(String bookingNo);

    List<Booking> findByUser(User user);

//    @Query("SELECT b FROM Booking b WHERE b.user = :user " +
//            "AND (:bookingNo IS NULL OR :bookingNo = '' OR LOWER(b.bookingNo) LIKE LOWER(CONCAT('%', :bookingNo, '%'))) " +
//            "AND (:status IS NULL OR :status = '' OR b.status = :status) " +
//            "AND (:startDate IS NULL OR b.createdAt >= :startDate) " +
//            "AND (:endDate IS NULL OR b.createdAt <= :endDate) " +
//            "AND (:tourDetailId IS NULL OR b.tourDetail.id = :tourDetailId) " +
//            "AND (:paymentMethod IS NULL OR :paymentMethod = '' OR b.paymentMethod = :paymentMethod) " +
//            "AND (:minAmount IS NULL OR b.totalAmount >= :minAmount) " +
//            "AND (:maxAmount IS NULL OR b.totalAmount <= :maxAmount)")
//    List<Booking> findAllByUser(@Param("user") User user,
//                                @Param("bookingNo") String bookingNo,
//                                @Param("status") String status,
//                                @Param("startDate") LocalDateTime startDate,
//                                @Param("endDate") LocalDateTime endDate,
//                                @Param("tourDetailId") String tourDetailId,
//                                @Param("paymentMethod") String paymentMethod,
//                                @Param("minAmount") BigDecimal minAmount,
//                                @Param("maxAmount") BigDecimal maxAmount,
//                                Sort sort);

    List<Booking> findByStatusIn(List<String> statuses);

    @Query("select b from Booking b where b.status = ?1 and b.tourDetail.endDate = ?2")
    List<Booking> findByStatusAndTourDetail_EndDate(String status, LocalDate endDate);

    long countByUser(User user);

    @Query("SELECT YEAR(b.createdAt) as year, SUM(b.totalAmount) as totalRevenue " +
            "FROM Booking b " +
            "WHERE b.status = 'COMPLETED' " +
            "GROUP BY YEAR(b.createdAt) " +
            "ORDER BY YEAR(b.createdAt) DESC")
    List<Object[]> findYearlyRevenue();

    @Query("SELECT b.status, COUNT(b) " +
            "FROM Booking b " +
            "GROUP BY b.status")
    List<Object[]> findBookingStatusStats();

    @Query("SELECT COUNT(b) FROM Booking b")
    long bookingCountStats(); // Để tính tổng số booking

    // Tổng số booking trong khoảng thời gian
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.createdAt >= :start AND b.createdAt <= :end")
    long countBookingsInPeriod(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // Tổng doanh thu trong khoảng thời gian
    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Booking b WHERE b.createdAt >= :start AND b.createdAt <= :end AND b.status IN :statuses")
    BigDecimal getRevenueInPeriod(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("statuses") List<String> statuses);

    // Số lượng booking bị hủy trong khoảng thời gian
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.createdAt >= :start AND b.createdAt <= :end AND b.status IN :statuses")
    long countCancelledBookingsInPeriod(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("statuses") List<String> statuses);


    @Query("SELECT new com.pacific.pacificbe.dto.TopTour(" +
            "td.tour.id, td.tour.title, COUNT(b)) " +
            "FROM Booking b " +
            "JOIN b.tourDetail td " +
            "WHERE b.createdAt >= :startDate AND b.createdAt <= :endDate " +
            "GROUP BY td.tour.id, td.tour.title " +
            "ORDER BY COUNT(b) DESC")
    List<TopTour> getTopBookedTours(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    @Query("select b from Booking b where b.status = ?1 and b.createdAt < ?2")
    List<Booking> findByStatusAndCreatedAtBefore(String status, LocalDateTime createdAt);

    @Modifying
    @Query("UPDATE Booking b SET b.status = :newStatus WHERE b.status = :currentStatus AND b.createdAt <= :createdAt")
    int updateStatusToExpired(@Param("newStatus") String newStatus,
                              @Param("currentStatus") String currentStatus,
                              @Param("createdAt") LocalDateTime createdAt);

    @Modifying
    @Query("UPDATE Booking b SET b.status = :newStatus WHERE b.status = :currentStatus AND b.tourDetail.status = :tourDetailStatus")
    int updateStatusByTourDetailStatus(@Param("newStatus") String newStatus,
                                       @Param("currentStatus") String currentStatus,
                                       @Param("tourDetailStatus") String tourDetailStatus);

    default int updateStatusToExpired(LocalDateTime createdAt) {
        return updateStatusToExpired(BookingStatus.EXPIRED.toString(),
                BookingStatus.PENDING.toString(), createdAt);
    }

    default int updateStatusToOngoing() {
        return updateStatusByTourDetailStatus(BookingStatus.ON_GOING.toString(),
                BookingStatus.PAID.toString(), TourDetailStatus.IN_PROGRESS.toString());
    }

    default int updateStatusToCompleted() {
        return updateStatusByTourDetailStatus(BookingStatus.COMPLETED.toString(),
                BookingStatus.ON_GOING.toString(), TourDetailStatus.CLOSED.toString());
    }

    @Query("select b from Booking b where b.status = ?1")
    List<Booking> findByStatus(String status);


    //    THOGN KE DOANH THU CHO ADMIN HOME
// Tổng doanh thu năm nay
    @Query(value = """
            SELECT SUM(b.total_amount)
            FROM booking b
            WHERE YEAR(b.created_at) = YEAR(GETDATE())
                AND b.status = 'COMPLETED'
            """, nativeQuery = true)
    BigDecimal getTotalRevenueThisYear();

    // Tổng doanh thu năm ngoái
    @Query(value = """
            SELECT SUM(b.total_amount)
            FROM booking b
            WHERE YEAR(b.created_at) = YEAR(GETDATE()) - 1
                AND b.status = 'COMPLETED'
            """, nativeQuery = true)
    BigDecimal getTotalRevenueLastYear();

    // Doanh thu từng tháng trong năm nay
    @Query(value = """
            SELECT 
                MONTH(b.created_at) AS month,
                SUM(b.total_amount) AS revenue
            FROM booking b
            WHERE YEAR(b.created_at) = YEAR(GETDATE())
                AND b.status = 'COMPLETED'
            GROUP BY MONTH(b.created_at)
            ORDER BY MONTH(b.created_at)
            """, nativeQuery = true)
    List<MonthlyRevenueDTO> getMonthlyRevenueThisYear();

    //    Tìm theo userId
    List<Booking> findByUserId(String userId);

    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND b.status = :status")
    List<Booking> findByUserIdAndStatus(@Param("userId") String userId, @Param("status") String status);

    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND b.status IN :statuses")
    List<Booking> findByUserIdAndStatuses(@Param("userId") String userId, @Param("statuses") List<String> statuses);

    @Query("select b from Booking b where b.user = ?1 order by b.updatedAt DESC")
    List<Booking> findByUserOrderByUpdatedAtDesc(User user);

    @Query("select b from Booking b where b.user.id = :userId and b.active = true and (coalesce(:bookingNo, '') = '' or b.bookingNo = :bookingNo) order by b.updatedAt DESC")
    List<Booking> findByUserAndBookingNoOrderByUpdatedAtDesc(@Param("userId") String userId, @Param("bookingNo") String bookingNo);
}