package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TourRepository extends JpaRepository<Tour, Integer> {
    Optional<Tour> findById(String tourName);
}
