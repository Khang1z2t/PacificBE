package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, String> {
}
