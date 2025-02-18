package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.response.TourResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.TourMapper;
import com.pacific.pacificbe.model.Tour;
import com.pacific.pacificbe.repository.TourRepository;
import com.pacific.pacificbe.services.TourService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TourServiceImpl implements TourService {
    TourRepository tourRepository;
    TourMapper tourMapper;

    @Override
    public List<TourResponse> getAllTours() {
        return tourMapper.toTourResponseList(tourRepository.findAll());
    }

    @Override
    public TourResponse getTourById(String id) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TOUR_NOT_FOUND));
        return tourMapper.toTourResponse(tour);
    }


    @Override
    public List<TourResponse> searchTours(String destination, LocalDate departureDate, LocalDate returnDate, BigDecimal minPrice, BigDecimal maxPrice) {
        return List.of();
    }
//
//    @Override
//    public List<TourResponse> searchTours(String destination, LocalDate departureDate, LocalDate returnDate,
//                                            BigDecimal minPrice, BigDecimal maxPrice) {
//        // Gọi đến phương thức searchTours của repository
//        List<Tour> tours = tourRepository.searchTours(destination, departureDate, returnDate, minPrice, maxPrice);
//        return tours.stream()
//                .map(this::mapToResponse)
//                .collect(Collectors.toList());
//    }

}
