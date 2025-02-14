package com.pacific.pacificbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pacific.pacificbe.model.Review;

public interface ReviewRepository extends JpaRepository<Review, String>{

}
