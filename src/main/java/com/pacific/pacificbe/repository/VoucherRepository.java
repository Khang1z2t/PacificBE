package com.pacific.pacificbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pacific.pacificbe.model.Voucher;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
}
