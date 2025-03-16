package com.pacific.pacificbe.controller;

import java.math.BigDecimal;
import java.util.List;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.request.CreateTourRequest;
import com.pacific.pacificbe.dto.request.TourFilterRequest;
import com.pacific.pacificbe.dto.response.TourByIdResponse;
import com.pacific.pacificbe.dto.response.TourResponse;
import com.pacific.pacificbe.services.GoogleDriveService;
import com.pacific.pacificbe.services.TourService;
import com.pacific.pacificbe.utils.UrlMapping;
import com.pacific.pacificbe.utils.enums.FolderType;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(UrlMapping.TOURS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TourController {

    TourService tourService;
    GoogleDriveService googleDriveService;

    @GetMapping(UrlMapping.GET_ALL_TOURS)
    @Operation(summary = "Lấy danh sách tour")
    public ResponseEntity<ApiResponse<List<TourResponse>>> getAllTours(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String categoryId
            ) {
        return ResponseEntity.ok(
                ApiResponse.<List<TourResponse>>builder()
                        .data(tourService.getAllTours(title, minPrice, maxPrice, categoryId))
                        .build()
        );
    }

    @GetMapping(UrlMapping.GET_TOUR_BY_ID)
    @Operation(summary = "Lấy tour theo id")
    public ResponseEntity<ApiResponse<TourByIdResponse>> getTourById(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(200, "Complete", tourService.getTourById(id)));
    }

    @GetMapping(UrlMapping.GET_TOUR_BY_CATEGORY)
    @Operation(summary = "Lấy tour theo category")
    public List<TourResponse> getTourCategory(@PathVariable("category") String category) {
        return tourService.getTourCategory(category);
    }

    @GetMapping(UrlMapping.GET_TOUR_BY_RATING)
    @Operation(summary = "Lấy tour theo rating")
    public List<TourResponse> getToursByRating(@PathVariable("rating") Double rating) {
        return tourService.getTourRating(rating);
    }

    @GetMapping(UrlMapping.GET_TOUR_BY_DESTINATION)
    @Operation(summary = "Lấy tour theo điểm đến")
    public List<TourResponse> getTourDestination(@PathVariable("destination") String destination) {
        return tourService.getTourDestination(destination);
    }

    @PostMapping(value = UrlMapping.ADD_TOUR, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Thêm tour (test thêm ảnh ở postman) ở FE lưu ý tour request là model attribute nên xài qs lib")
    public ResponseEntity<ApiResponse<TourResponse>> createTour(@ModelAttribute CreateTourRequest request,
                                                                @RequestParam(required = false) MultipartFile thumbnail,
                                                                @RequestParam(required = false) MultipartFile[] images) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Hoàn thành", tourService.createTour(request, thumbnail, images)));
    }

    @PostMapping(value = UrlMapping.ADD_TOUR_THUMBNAIL, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Thêm ảnh chính cho tour")
    public ResponseEntity<ApiResponse<TourResponse>> addTourThumbnail(@PathVariable String id,
                                                                      @RequestParam("thumbnail") MultipartFile thumbnail) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Hoàn thành", tourService.addTourThumbnail(id, thumbnail)));
    }

    @PostMapping(value = UrlMapping.ADD_TOUR_IMAGES, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Thêm nhiều ảnh phụ cho tour")
    public ResponseEntity<ApiResponse<TourResponse>> addTourImages(@PathVariable String id,
                                                                   @RequestParam("images") MultipartFile[] images) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Hoàn thành", tourService.addTourImages(id, images)));
    }

    @PostMapping(value = "/test-img", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> testImg(@RequestParam("file") MultipartFile file) {
//        https://drive.google.com/file/d/1GTrRDjzn82Kei0vsBQ7FGLN14iRlt5-m/view?usp=drivesdk
        return ResponseEntity.ok(googleDriveService.uploadImageToDrive(file, FolderType.TOUR));
    }
}
