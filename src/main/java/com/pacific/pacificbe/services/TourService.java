package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.response.TourResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TourService {
    List<TourResponse> getAllTours();

    TourResponse getTourById(String id);
    
    List<TourResponse> searchTours(String destination, 
                                   LocalDate departureDate, 
                                   LocalDate returnDate, 
                                   BigDecimal minPrice, 
                                   BigDecimal maxPrice);
}
