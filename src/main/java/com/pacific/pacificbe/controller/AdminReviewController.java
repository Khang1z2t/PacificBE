package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.request.AdminReviewRequest;
import com.pacific.pacificbe.dto.response.AdminReviewResponse;
import com.pacific.pacificbe.services.AdminReviewService;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UrlMapping.ADMIN_REVIEW)
@RequiredArgsConstructor
@Tag(name = "Admin Review Controller", description = "Các thao tác CRUD cho đánh giá của admin")
public class AdminReviewController {
    private final AdminReviewService adminReviewService;

    @Operation(
            summary = "Tạo mới đánh giá của admin",
            description = "Tạo mới một đánh giá của admin và trả về thông tin đánh giá vừa tạo"
    )
    @PostMapping(UrlMapping.CREATE_ADMIN_REVIEW)
    public ResponseEntity<ApiResponse<AdminReviewResponse>> createAdminReview(@RequestBody AdminReviewRequest request) {
        AdminReviewResponse response = adminReviewService.createReview(request);
        return ResponseEntity.ok(ApiResponse.<AdminReviewResponse>builder().data(response).build());
    }

    @Operation(
            summary = "Lấy đánh giá của admin theo ID",
            description = "Trả về thông tin chi tiết của đánh giá dựa trên ID"
    )
    @GetMapping(UrlMapping.GET_ADMIN_REVIEW_BY_ID)
    public ResponseEntity<ApiResponse<AdminReviewResponse>> getAdminReviewById(@PathVariable String id) {
        AdminReviewResponse response = adminReviewService.getReviewById(id);
        return ResponseEntity.ok(ApiResponse.<AdminReviewResponse>builder().data(response).build());
    }

    @Operation(
            summary = "Lấy danh sách đánh giá của admin",
            description = "Trả về danh sách tất cả các đánh giá của admin"
    )
    @GetMapping(UrlMapping.GET_ALL_ADMIN_REVIEW)
    public ResponseEntity<ApiResponse<List<AdminReviewResponse>>> getAllAdminReviews() {
        List<AdminReviewResponse> responses = adminReviewService.getAllReviews();
        return ResponseEntity.ok(ApiResponse.<List<AdminReviewResponse>>builder().data(responses).build());
    }

    @Operation(
            summary = "Cập nhật đánh giá của admin",
            description = "Cập nhật thông tin đánh giá dựa trên ID"
    )
    @PutMapping(UrlMapping.UPDATE_ADMIN_REVIEW)
    public ResponseEntity<ApiResponse<AdminReviewResponse>> updateAdminReview(@PathVariable String id,
                                                                              @RequestBody AdminReviewRequest request) {
        AdminReviewResponse response = adminReviewService.updateReview(id, request);
        return ResponseEntity.ok(ApiResponse.<AdminReviewResponse>builder().data(response).build());
    }

    @Operation(
            summary = "Xóa đánh giá của admin",
            description = "Xóa đánh giá dựa trên ID"
    )
    @DeleteMapping(UrlMapping.DELETE_ADMIN_REVIEW)
    public ResponseEntity<Void> deleteAdminReview(@PathVariable String id) {
        adminReviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
