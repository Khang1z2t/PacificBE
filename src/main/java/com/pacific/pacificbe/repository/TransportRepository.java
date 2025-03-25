package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.dto.response.TransportResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportRepository extends JpaRepository<TransportResponse, String> {
    boolean existsById(String id);
}
