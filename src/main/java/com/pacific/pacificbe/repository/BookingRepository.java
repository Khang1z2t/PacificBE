package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.dto.response.MonthlyRevenue;
import com.pacific.pacificbe.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, String>{
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
}
