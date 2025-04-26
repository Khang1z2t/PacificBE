package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SubscriberRepository extends JpaRepository<Subscriber, String> {
  @Query("select s from Subscriber s where s.unsubscribeToken = ?1")
  Optional<Subscriber> findByUnsubscribeToken(String unsubscribeToken);

  @Query("select (count(s) > 0) from Subscriber s where s.email = ?1")
  boolean existsByEmail(String email);

  @Query("select s from Subscriber s where s.email = ?1")
  Optional<Subscriber> findByEmail(String email);
}