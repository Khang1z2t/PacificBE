package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.Payment;
import com.pacific.pacificbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findAllByUser(User user);

    Optional<Payment> findByBookingIdAndStatus(String bookingId, String status);

    @Query("select p from Payment p where p.booking.id = ?1 and p.status = ?2")
    Optional<Payment> findByBooking_IdAndStatus(String id, String status);

    @Query("select (count(p) > 0) from Payment p where p.txnRef = ?1")
    boolean existsByTxnRef(String txnRef);

    @Query("select p from Payment p where p.txnRef = ?1")
    Optional<Payment> findByTxnRef(String txnRef);

    @Query("select p from Payment p where p.status = ?1 and p.expireAt < ?2")
    List<Payment> findByStatusAndExpireAtBefore(String status, LocalDateTime expireAt);

    List<Payment> findByStatus(String status);
}
