package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.TourDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourDetailRepository extends JpaRepository<TourDetail, String> {
    List<TourDetail> findByTourId(String tourId);

    void deleteByTourId(String tourId);
}
