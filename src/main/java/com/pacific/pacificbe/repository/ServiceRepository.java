package com.pacific.pacificbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pacific.pacificbe.model.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {
}
