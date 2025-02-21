package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.TourRequest;
import com.pacific.pacificbe.dto.response.TourResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TourService {
    List<TourResponse> getAllTours();

    TourResponse getTourById(String id);
    
    TourResponse createTour(TourRequest request);
    
    List<TourResponse> getTourCategory(String category);
    
    List<TourResponse> getTourRating(Double rating);
    
    List<TourResponse> getTourDestination(String destination);
}
