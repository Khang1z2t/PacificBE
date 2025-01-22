package com.pacific.pacificbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pacific.pacificbe.model.Transportation;

@Repository
public interface TransportationRepository extends JpaRepository<Transportation, Integer> {
}
