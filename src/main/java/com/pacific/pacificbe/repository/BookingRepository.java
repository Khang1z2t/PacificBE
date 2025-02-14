package com.pacific.pacificbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pacific.pacificbe.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, String>{

}
