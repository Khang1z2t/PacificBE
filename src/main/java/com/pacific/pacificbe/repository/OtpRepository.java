package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.Otp;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, String> {
    Optional<Otp> findByEmail(String userId);

    Optional<Otp> findByEmailAndExpiresAtAfter(String email, Instant expiresAtAfter);
    void deleteByEmail(String userId);

    @Modifying
    @Transactional
    void deleteByExpiresAtBefore(Instant expiryTime);
}
