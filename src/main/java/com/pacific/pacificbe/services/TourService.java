package com.pacific.pacificbe.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pacific.pacificbe.repository.TourRepository;

import java.math.BigDecimal;
import java.sql.Date;

@Service
public class TourService {

    @Autowired
    private TourRepository tourRepository;

    public void manageTour(String action, Integer tourID, String tourName, String destination, Date startDate, Date endDate, BigDecimal price, Integer capacity, String description, String imageURL, Integer statusID, Integer regionID, Integer tourTypeID) {
        tourRepository.manageTour(action, tourID, tourName, destination, startDate, endDate, price, capacity, description, imageURL, statusID, regionID, tourTypeID);
    }
}

