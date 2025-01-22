package com.pacific.pacificbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pacific.pacificbe.model.TourGuide;

@Repository
public interface TourGuideRepository extends JpaRepository<TourGuide, Integer> {
}
