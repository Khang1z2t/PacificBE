package com.pacific.pacificbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pacific.pacificbe.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
