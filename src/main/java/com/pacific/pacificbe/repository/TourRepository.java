package com.pacific.pacificbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pacific.pacificbe.model.Tour;

@Repository
public interface TourRepository extends JpaRepository<Tour, Integer> {
}
