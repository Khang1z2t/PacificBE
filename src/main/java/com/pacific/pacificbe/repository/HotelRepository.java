package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, String> {
    List<Hotel> findByNameContainingAndCostBetweenAndTypeHotel(
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String typeHotel
    );
}