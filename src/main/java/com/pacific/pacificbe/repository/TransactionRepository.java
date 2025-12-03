package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, String> {


    Transaction findByBookingIdAndType(String bookingId, String type);

    List<Transaction> findByStatus(String status);

    List<Transaction> findByBookingId(String bookingId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = 'REFUNDED' AND t.status = 'COMPLETED'")
    Optional<BigDecimal> sumRefundedAmount();

    @Query(value = "SELECT wt.id FROM transactions wt WHERE wt.id LIKE CONCAT('T', FORMAT(NOW(), 'ddMMyy'), '%') ORDER BY CAST(SUBSTRING(wt.id, 8, 4) AS INTEGER) DESC LIMIT 1", nativeQuery = true)
    String findLatestWalletTransactionIdOfToday();

    @Query("select t from Transaction t where t.user.id = ?1")
    List<Transaction> findByUser_Id(String id);

    @Query("select t from Transaction t where t.booking.id = ?1 and t.status = ?2")
    Transaction findByBooking_IdAndStatus(String id, String status);
}