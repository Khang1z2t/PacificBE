package com.pacific.pacificbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pacific.pacificbe.model.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {
}
