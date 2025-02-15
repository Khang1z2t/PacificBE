package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, String>{

}
