package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.Guide;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuideRepository extends JpaRepository<Guide, String> {
}
