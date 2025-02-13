package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.response.TourResponse;
import com.pacific.pacificbe.model.Tour;
import com.pacific.pacificbe.repository.TourRepository;
import com.pacific.pacificbe.services.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;

    @Override
    public List<TourResponse> getAllTours() {
        List<Tour> tours = tourRepository.findAll();
        return tours.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TourResponse getTourById(String id) {
        // Chuyển đổi id từ String sang Integer vì repository sử dụng Integer
        Integer tourId;
        try {
            tourId = Integer.valueOf(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid tour id: " + id);
        }
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new RuntimeException("Tour not found with id: " + id));
        return mapToResponse(tour);
    }

    @Override
    public List<TourResponse> searchTours(String destination, LocalDate departureDate, LocalDate returnDate,
                                            BigDecimal minPrice, BigDecimal maxPrice) {
        // Gọi đến phương thức searchTours của repository
        List<Tour> tours = tourRepository.searchTours(destination, departureDate, returnDate, minPrice, maxPrice);
        return tours.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Phương thức chuyển đổi từ entity Tour sang DTO TourResponse
    private TourResponse mapToResponse(Tour tour) {
        return TourResponse.builder()
                .id(tour.getId())  // id đã là String vì dùng UUID
                .title(tour.getTitle())
                .description(tour.getDescription() != null ? tour.getDescription() : "")
                .themeUrl(tour.getThemeUrl() != null ? tour.getThemeUrl() : "")
                // capacity: chuyển từ quantity (Long) sang String
                .capacity(tour.getQuantity() != null ? String.valueOf(tour.getQuantity()) : "0")
                // Sử dụng getBasePrice() cho basePrice
                .basePrice(tour.getBasePrice() != null ? tour.getBasePrice().doubleValue() : 0.0)
                .childrenPrice(tour.getChildrenPrice() != null ? tour.getChildrenPrice().doubleValue() : 0.0)
                .duration(tour.getDuration() != null ? tour.getDuration() : "")
                .destination(tour.getDestination() != null ? tour.getDestination() : "")
                // Lưu ý: trong entity, trường meeting point có tên là mettingPoint, vì vậy ta sử dụng getMettingPoint()
                .meetingPoint(tour.getMettingPoint() != null ? tour.getMettingPoint() : "")
                .status(tour.getStatus() != null ? tour.getStatus() : "")
                // Nếu không có dữ liệu tourImages hoặc guides, trả về danh sách rỗng
                .tourImages(tour.getTourImages() != null ? List.copyOf(tour.getTourImages()) : Collections.emptyList())
                .guides(tour.getGuides() != null ? List.copyOf(tour.getGuides()) : Collections.emptyList())
                .build();
    }

}
