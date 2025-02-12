package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.response.TourResponse;
import com.pacific.pacificbe.mapper.TourMapper;
import com.pacific.pacificbe.repository.TourRepository;
import com.pacific.pacificbe.services.TourService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TourServiceImpl implements TourService {
    TourRepository tourReponsitory;
    TourMapper tourMapper;

    @Override
    public List<TourResponse> getAllTours() {
        var tours = tourReponsitory.findAll();
        return tours.stream().map(tourMapper::toTourResponse).toList();
    }

    @Override
    public TourResponse getTourById(String id) {
        var tour = tourReponsitory.findById(id).orElseThrow(
                () -> new RuntimeException("Tour not found")
        );

        return tourMapper.toTourResponse(tour);
    }
}
