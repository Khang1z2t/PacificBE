package com.pacific.pacificbe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pacific.pacificbe.model.Tours;

public interface TourRepository extends JpaRepository<Tours, Long> {
    // Lấy tất cả các tour
    @Query("SELECT t FROM Tours t")
    List<Tours> findAllTours();
    
}
