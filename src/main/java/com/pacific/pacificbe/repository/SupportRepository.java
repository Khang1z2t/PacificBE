package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.Support;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SupportRepository extends JpaRepository<Support, String> {
    Optional<Support> findByUserEmail(String email);
}
