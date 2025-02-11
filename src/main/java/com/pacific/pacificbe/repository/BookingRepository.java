package com.pacific.pacificbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pacific.pacificbe.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
}
