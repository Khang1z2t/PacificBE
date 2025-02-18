package com.pacific.pacificbe.controller;

import java.util.List;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.response.TourResponse;
import com.pacific.pacificbe.services.TourService;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UrlMapping.TOURS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ToursController {

    TourService tourService;

    @GetMapping(UrlMapping.GET_ALL_TOURS)
    @Operation(summary = "Lấy danh sách tour")
    public ResponseEntity<ApiResponse<List<TourResponse>>> getAllTours() {
        return ResponseEntity.ok(
                ApiResponse.<List<TourResponse>>builder()
                        .data(tourService.getAllTours())
                        .build()
        );
    }

    @GetMapping(UrlMapping.GET_TOUR_BY_ID)
    @Operation(summary = "Lấy tour theo id")
    public ResponseEntity<ApiResponse<TourResponse>> getTourById(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(200, "Complete", tourService.getTourById(id)));
    }
}
