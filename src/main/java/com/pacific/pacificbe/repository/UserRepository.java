package com.pacific.pacificbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pacific.pacificbe.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
