package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.Blog;
import com.pacific.pacificbe.model.BlogCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.pacific.pacificbe.utils.enums.BlogStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, String> {
    boolean existsByTitle(String title);

    Optional<Blog> findByTitle(String title);

    @Query("select (count(b) > 0) from Blog b where b.slug = ?1")
    boolean existsBySlug(String slug);

    @Query("select b from Blog b where b.slug = ?1")
    Optional<Blog> findBySlug(String slug);

    @Query("select b from Blog b where b.category = ?1")
    List<Blog> findByCategory(BlogCategory category);
}
