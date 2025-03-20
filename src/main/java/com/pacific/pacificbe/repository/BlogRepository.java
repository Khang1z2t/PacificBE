package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pacific.pacificbe.utils.enums.BlogStatus;

import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, String> {
    boolean existsByTitle(String title);
    boolean existsByAuthor(String author);
    boolean existsByTitleAndAuthor(String title, String author);

    Optional<Blog> findByTitle(String title);
    Optional<Blog> findByAuthor(String author);

    Optional<Blog> findByTitleOrAuthor(String title, String author);
}
