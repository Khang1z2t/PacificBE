package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.Guide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuideRepository extends JpaRepository<Guide, String> {
    boolean existsByLastName(String lastName);

    boolean existsByEmail(String email);

    boolean existsByLastNameAndEmail(String lastName, String email);

    Optional<Guide> findByLastNameAndEmail(String lastName, String email);

}
