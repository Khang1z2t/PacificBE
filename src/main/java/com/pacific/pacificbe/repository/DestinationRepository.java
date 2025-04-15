package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.Destination;
import com.pacific.pacificbe.dto.response.TopDestination;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, String> {

    //    Lấy top Destination
    @Query("SELECT new com.pacific.pacificbe.dto.response.TopDestination(" +
            "d.id, d.city, d.country, COUNT(b.id)) " +
            "FROM Destination d " +
            "JOIN Tour t ON t.destination.id = d.id " +
            "JOIN Booking b ON b.tourDetail.tour.id = t.id " +
            "GROUP BY d.id, d.city, d.country " +
            "ORDER BY COUNT(b.id) DESC")
    List<TopDestination> findTopDestinationsByBookingCount();

}
