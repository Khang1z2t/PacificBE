package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TourRepository extends JpaRepository<Tour, String> {
    List<Tour> findToursByActiveIsTrue();

    @Procedure(procedureName = "FineTourCategory")
    List<Tour> findTourCategory(@Param("category") String category);
    
    @Procedure(procedureName = "FindTourRating")
    List<Tour> findTourRating(@Param("rating") Double rating);
    
    @Procedure(procedureName = "FindTourDestination")
    List<Tour> findTourDestination(@Param("destination") String destination);
}
