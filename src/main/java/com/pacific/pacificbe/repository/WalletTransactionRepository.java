package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, String> {


    WalletTransaction findByBookingIdAndType(String bookingId, String type);

    List<WalletTransaction> findByStatus(String status);

    List<WalletTransaction> findByBookingId(String bookingId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM WalletTransaction t WHERE t.type = 'REFUNDED' AND t.status = 'COMPLETED'")
    Optional<BigDecimal> sumRefundedAmount();
}