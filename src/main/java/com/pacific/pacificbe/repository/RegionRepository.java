package com.pacific.pacificbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pacific.pacificbe.model.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Integer> {
}
