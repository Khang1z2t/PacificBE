package com.pacific.pacificbe.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.pacific.pacificbe.model.User;
import jakarta.persistence.NamedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    // Số lượng khách hàng mới hôm nay
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :startOfDay AND u.createdAt <= :endOfDay")
    long countNewUsersToday(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    // Số lượng khách hàng mới hôm qua
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :startOfYesterday AND u.createdAt <= :endOfYesterday")
    long countNewUsersYesterday(@Param("startOfYesterday") LocalDateTime startOfYesterday, @Param("endOfYesterday") LocalDateTime endOfYesterday);
}
