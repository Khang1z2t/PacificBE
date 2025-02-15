package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String>{

}
