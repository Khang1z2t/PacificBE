package com.pacific.pacificbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pacific.pacificbe.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, String>{

}
