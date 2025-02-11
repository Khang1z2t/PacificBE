package com.pacific.pacificbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pacific.pacificbe.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}

