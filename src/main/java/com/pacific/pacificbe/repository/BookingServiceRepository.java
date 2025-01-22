package com.pacific.pacificbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pacific.pacificbe.model.BookingService;

@Repository
public interface BookingServiceRepository extends JpaRepository<BookingService, Integer> {
}

