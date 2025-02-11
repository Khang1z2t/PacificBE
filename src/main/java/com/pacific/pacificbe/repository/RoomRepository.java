package com.pacific.pacificbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pacific.pacificbe.model.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
}

