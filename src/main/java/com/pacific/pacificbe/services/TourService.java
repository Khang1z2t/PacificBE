package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.CreateTourRequest;
import com.pacific.pacificbe.dto.response.TourByIdResponse;
import com.pacific.pacificbe.dto.response.TourResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TourService {
    List<TourResponse> getAllTours();

    TourByIdResponse getTourById(String id);
    
    TourResponse createTour(CreateTourRequest request, MultipartFile thumbnail);
    
    List<TourResponse> getTourCategory(String category);
    
    List<TourResponse> getTourRating(Double rating);
    
    List<TourResponse> getTourDestination(String destination);

    TourResponse addTourThumbnail(String id, MultipartFile thumbnail);

    TourResponse addTourImages(String id, MultipartFile[] images);
}
