package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.CreateTourRequest;
import com.pacific.pacificbe.dto.response.TourResponse;

import java.util.List;

public interface TourService {
    List<TourResponse> getAllTours();

    TourResponse getTourById(String id);
    
    TourResponse createTour(CreateTourRequest request);
    
    List<TourResponse> getTourCategory(String category);
    
    List<TourResponse> getTourRating(Double rating);
    
    List<TourResponse> getTourDestination(String destination);
}
