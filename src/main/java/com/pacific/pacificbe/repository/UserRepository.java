package com.pacific.pacificbe.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.pacific.pacificbe.model.User;
import jakarta.persistence.NamedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsernameAndEmail(String username, String email);

    Optional<User> findByUsername(String username);

//    @Query(name = "User.findByEmail")
    Optional<User> findByEmail(@Param("email") String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

}
