package com.pacific.pacificbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pacific.pacificbe.model.BookingVoucher;

@Repository
public interface BookingVoucherRepository extends JpaRepository<BookingVoucher, Integer> {
}
