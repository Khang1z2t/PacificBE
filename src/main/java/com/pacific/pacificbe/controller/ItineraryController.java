package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.response.showTour.DetailTourResponse;
import com.pacific.pacificbe.dto.response.showTour.ItineraryTourDetailResponse;
import com.pacific.pacificbe.services.ItineraryService;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlMapping.ITINERARY)
@RequiredArgsConstructor
public class ItineraryController {
    private final ItineraryService itineraryService;

    @GetMapping(UrlMapping.ITINERARY_TOUR_AND_DATE)
    @Operation(summary = "Lấy lịch trình theo tour và ngày")
    public ResponseEntity<ApiResponse<List<ItineraryTourDetailResponse>>> getItineraryByTourAndDate(
            @PathVariable String tourId,
            @PathVariable String createdDay
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                200, "Lấy chi tiết tour theo ngày thành công",
                itineraryService.getItineraryByTourAndDate(tourId,createdDay)));
    }
}
