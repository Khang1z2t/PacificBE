package com.pacific.pacificbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pacific.pacificbe.model.Tour;

@Repository
public interface TourRepository extends JpaRepository<Tour, Integer> {
    @Procedure(procedureName = "dbo.ManageTour")
    void manageTour(@Param("Action") String action,
                    @Param("TourID") Integer tourID,
                    @Param("TourName") String tourName,
                    @Param("Destination") String destination,
                    @Param("StartDate") java.sql.Date startDate,
                    @Param("EndDate") java.sql.Date endDate,
                    @Param("Price") java.math.BigDecimal price,
                    @Param("Capacity") Integer capacity,
                    @Param("Description") String description,
                    @Param("ImageURL") String imageURL,
                    @Param("StatusID") Integer statusID,
                    @Param("RegionID") Integer regionID,
                    @Param("TourTypeID") Integer tourTypeID);
}
