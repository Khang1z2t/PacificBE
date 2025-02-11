package com.pacific.pacificbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pacific.pacificbe.model.TourGuideAssignment;

@Repository
public interface TourGuideAssignmentRepository extends JpaRepository<TourGuideAssignment, Integer> {
}
