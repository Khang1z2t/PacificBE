package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingDetailRepository extends JpaRepository<BookingDetail, String> {
}