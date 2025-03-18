package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.CreateTourRequest;
import com.pacific.pacificbe.dto.request.TourFilterRequest;
import com.pacific.pacificbe.dto.request.UpdateTourRequest;
import com.pacific.pacificbe.dto.response.TourByIdResponse;
import com.pacific.pacificbe.dto.response.TourResponse;
import com.pacific.pacificbe.dto.response.showTour.ItineraryTourDetailResponse;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface TourService {
    List<TourResponse> getAllTours(String title, BigDecimal minPrice, BigDecimal maxPrice, String categoryId);

    TourByIdResponse getTourById(String id);
    
    TourResponse createTour(CreateTourRequest request, MultipartFile thumbnail, MultipartFile[] images);
    
    List<TourResponse> getTourCategory(String category);
    
    List<TourResponse> getTourRating(Double rating);
    
    List<TourResponse> getTourDestination(String destination);

    TourResponse addTourThumbnail(String id, MultipartFile thumbnail);

    TourResponse addTourImages(String id, MultipartFile[] images);

    TourResponse updateTour(String id, UpdateTourRequest request, MultipartFile thumbnail, MultipartFile[] images);

    Boolean deleteTour(String id);

    Boolean deleteTourForce(String id);
}
