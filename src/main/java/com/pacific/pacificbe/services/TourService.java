package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.CreateTourRequest;
import com.pacific.pacificbe.dto.request.UpdateTourRequest;
import com.pacific.pacificbe.dto.response.TourByIdResponse;
import com.pacific.pacificbe.dto.response.TourResponse;
import com.pacific.pacificbe.dto.response.showTour.TourBookingCount;
import com.pacific.pacificbe.dto.response.showTour.TourDateResponse;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TourService {
    List<TourResponse> getAllTours(String title, BigDecimal minPrice, BigDecimal maxPrice, String categoryId, LocalDateTime startDate, LocalDateTime endDate);

    TourByIdResponse getTourById(String id);

    TourResponse createTour(CreateTourRequest request, MultipartFile thumbnail, MultipartFile[] images);

    TourResponse addTourThumbnail(String id, MultipartFile thumbnail);

    TourResponse addTourImages(String id, MultipartFile[] images);

    List<TourBookingCount> getTourBookingCounts(String tourId);

    TourResponse updateTour(String id, UpdateTourRequest request, MultipartFile thumbnail, MultipartFile[] images);

    Boolean deleteTour(String id, boolean active);

    Boolean deleteTourForce(String id);

    List<TourDateResponse> getToursByDate(LocalDateTime startDate, LocalDateTime endDate);

    TourResponse getTourByTourDetailId(String tourDetailId);

}
