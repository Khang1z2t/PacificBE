package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.request.ItineraryRequest;
import com.pacific.pacificbe.dto.request.ItineraryUpdateRequest;
import com.pacific.pacificbe.dto.response.ItineraryResponse;
import com.pacific.pacificbe.dto.response.showTour.ItineraryTourDetailResponse;
import com.pacific.pacificbe.model.Itinerary;
import com.pacific.pacificbe.services.ItineraryService;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(UrlMapping.ITINERARY)
public class ItineraryController {
    private final ItineraryService itineraryService;

//    @GetMapping(UrlMapping.ITINERARY_TOUR_AND_DATE)
//    @Operation(summary = "Lấy lịch trình theo tour và ngày")
//    public ResponseEntity<ApiResponse<List<ItineraryTourDetailResponse>>> getItineraryByTourAndDate(
//            @PathVariable String tourId,
//            @PathVariable String createdDay
//    ) {
//        return ResponseEntity.ok(new ApiResponse<>(
//                200, "Lấy chi tiết tour theo ngày thành công",
//                itineraryService.getItineraryByTourAndDate(tourId,createdDay)));
//    }

    @Operation(summary = "Lấy tất cả lịch trình")
    @GetMapping(UrlMapping.ITINERARY_ALL)
    public ResponseEntity<ApiResponse<List<Itinerary>>> getAllItinerary() {
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy tất cả lịch trình thành công",
                itineraryService.getAll()));
    }

    @Operation(summary = "Lấy lịch trình theo tour")
    @GetMapping(UrlMapping.ITINERARY_BY_TOUR)
    public ResponseEntity<ApiResponse<List<ItineraryResponse>>> getItineraryByTourId(@PathVariable String tourId) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy lịch trình theo tour thành công",
                itineraryService.getByTourId(tourId)));
    }

    @Operation(summary = "Thêm lịch trình")
    @PostMapping(UrlMapping.ITINERARY_ADD)
    public ResponseEntity<ApiResponse<List<ItineraryResponse>>> addItinerary(@RequestParam String tourId, @RequestBody ItineraryRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Thêm lịch trình thành công",
                itineraryService.addItinerary(tourId, request)));
    }

    @Operation(summary = "Cập nhật lịch trình")
    @PutMapping(UrlMapping.ITINERARY_UPDATE)
    public ResponseEntity<ApiResponse<ItineraryResponse>> updateItinerary(@PathVariable String id, @RequestBody ItineraryUpdateRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật lịch trình thành công", itineraryService.updateItinerary(id, request)));
    }

    @Operation(summary = "Xóa lịch trình")
    @DeleteMapping(UrlMapping.ITINERARY_DELETE)
    public ResponseEntity<ApiResponse<String>> deleteItinerary(@RequestParam String tourId) {
        itineraryService.deleteItinerary(tourId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Xóa lịch trình thành công", "Lịch trình đã được xóa"));
    }
}
