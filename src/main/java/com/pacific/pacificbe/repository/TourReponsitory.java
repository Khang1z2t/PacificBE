package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.Tour;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TourReponsitory extends JpaRepository<Tour, String> {
    Optional<Tour> findById(String id);
}
