package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Integer> {
    
    @Query("SELECT DISTINCT t FROM Tour t " +
           "LEFT JOIN t.schedules s " +
           "WHERE (:destination IS NULL OR t.destination LIKE %:destination%) " +
           "AND (:departureDate IS NULL OR s.startDate >= :departureDate) " +
           "AND (:returnDate IS NULL OR s.endDate <= :returnDate) " +
           "AND (:minPrice IS NULL OR t.basePrice >= :minPrice) " +
           "AND (:maxPrice IS NULL OR t.basePrice <= :maxPrice)")
    List<Tour> searchTours(
            @Param("destination") String destination,
            @Param("departureDate") LocalDate departureDate,
            @Param("returnDate") LocalDate returnDate,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice);
}
