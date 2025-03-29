package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.request.AddReviewRequest;
import com.pacific.pacificbe.dto.request.ReviewRequest;
import com.pacific.pacificbe.dto.request.UpdateStatusReviewRequest;
import com.pacific.pacificbe.dto.response.ReviewResponse;
import com.pacific.pacificbe.dto.response.ReviewResponseBooking;
import com.pacific.pacificbe.repository.ReviewRepository;
import com.pacific.pacificbe.services.ReviewService;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlMapping.RATING)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewController {
    ReviewService reviewService;
    private final ReviewRepository reviewRepository;

    @GetMapping(UrlMapping.GET_ALL_RATINGS)
    @Operation(summary = "Lấy danh sách tất cả đánh giá")
    ResponseEntity<ApiResponse<List<ReviewResponse>>> getAllRatings() {
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy danh sách thành công!", reviewService.getAllRatings()));
    }

    @GetMapping(UrlMapping.GET_RATING_BY_ID)
    @Operation(summary = "Lấy thông tin đánh giá theo ID")
    public ResponseEntity<ApiResponse<ReviewResponse>> getRatingById(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(200, "Complete", reviewService.getRatingById(id)));
    }

    @PatchMapping(UrlMapping.UPDATE_STATUS_RATING)
    @Operation(summary = "Cập nhật trạng thái đánh giá")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateStatusReview(
            @PathVariable String id,
            @RequestBody UpdateStatusReviewRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật trạng thái thành công", reviewService.updateStatus(id, request)));
    }

    @DeleteMapping(UrlMapping.DELETE_RATING)
    @Operation(summary = "Xóa đánh giá theo ID")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable String id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Xóa đánh giá thành công", null));
    }

    @GetMapping(UrlMapping.GET_RATING_BY_TOUR)
    @Operation(summary = "Lấy thông tin đánh giá tour")
    public ResponseEntity<List<ReviewResponse>> getAllReviews() {
        List<ReviewResponse> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @PostMapping(UrlMapping.ADD_RATING)
    @Operation(summary = "Tạo mới đánh giá")
    public ResponseEntity<ApiResponse<ReviewResponseBooking>> createReview(@RequestBody AddReviewRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Tạo mới đánh giá thành công",
                reviewService.createReviewByUser(request)));
    }
}
