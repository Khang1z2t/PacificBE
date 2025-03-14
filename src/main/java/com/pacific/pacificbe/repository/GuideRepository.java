package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.Guide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GuideRepository extends JpaRepository<Guide, String> {
    boolean existsByLastName(String lastName);

    boolean existsByEmail(String email);

    boolean existsByLastNameAndEmail(String lastName, String email);

//    @Query(value = "select u from Guide u where u.")
    Optional<Guide> findByLastNameAndEmail(String lastName, String email);
}
