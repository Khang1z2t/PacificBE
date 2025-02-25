package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {

}
