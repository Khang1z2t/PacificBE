package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.Payment;
import com.pacific.pacificbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String>{
    List<Payment> findAllByUser(User user);

    @Query("select p from Payment p inner join p.bookings bookings where bookings.user.id = ?1 and p.user.id = ?2")
    List<Payment> findByUserId(@Nullable String id, @Nullable String id1);
}
