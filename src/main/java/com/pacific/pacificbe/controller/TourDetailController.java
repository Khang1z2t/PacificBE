package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.request.CreateTourDetailRequest;
import com.pacific.pacificbe.dto.response.TourDetailResponse;
import com.pacific.pacificbe.services.TourDetailService;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlMapping.TOUR_DETAILS)
@RequiredArgsConstructor
public class TourDetailController {
    private final TourDetailService tourDetailService;

    @PostMapping(UrlMapping.ADD_TOUR_DETAIL)
    @Operation(summary = "Thêm mới chi tiết tour")
    public ResponseEntity<ApiResponse<TourDetailResponse>> addTourDetail(@RequestBody CreateTourDetailRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Thêm mới chi tiết tour thành công", tourDetailService.addTourDetail(request)));
    }

    @GetMapping(UrlMapping.GET_ALL_TOUR_DETAILS)
    @Operation(summary = "Lấy danh sách chi tiết tour")
    public ResponseEntity<ApiResponse<List<TourDetailResponse>>> getAllTourDetails() {
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy danh sách chi tiết tour thành công", tourDetailService.getAll()));
    }

    @GetMapping(UrlMapping.GET_TOUR_DETAIL_BY_ID)
    @Operation(summary = "Lấy chi tiết tour theo id")
    public ResponseEntity<ApiResponse<TourDetailResponse>> getTourDetailById(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy chi tiết tour thành công", tourDetailService.getTourDetailById(id)));
    }

    @GetMapping(UrlMapping.GET_TOUR_DETAIL_BY_TOUR)
    @Operation(summary = "Lấy chi tiết tour theo tour")
    public ResponseEntity<ApiResponse<List<TourDetailResponse>>> getTourDetailByTour(@PathVariable String tourId) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy chi tiết tour thành công", tourDetailService.getTourDetailByTourId(tourId)));
    }
}
