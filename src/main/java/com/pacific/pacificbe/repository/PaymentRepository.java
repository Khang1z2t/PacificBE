package com.pacific.pacificbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pacific.pacificbe.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}

