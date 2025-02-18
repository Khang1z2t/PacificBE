package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TourIMGRepository extends JpaRepository<Image, String>{
    List<Image> findByTourId(String tourId);
}
