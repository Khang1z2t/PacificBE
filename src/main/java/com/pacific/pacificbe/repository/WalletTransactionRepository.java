package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, String> {


    WalletTransaction findByBookingIdAndType(String bookingId, String type);

    List<WalletTransaction> findByStatus(String status);

    List<WalletTransaction> findByBookingId(String bookingId);
}