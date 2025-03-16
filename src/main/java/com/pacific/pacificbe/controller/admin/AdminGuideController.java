package com.pacific.pacificbe.controller.admin;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.request.*;
import com.pacific.pacificbe.dto.response.GuideResponse;
import com.pacific.pacificbe.services.GuideService;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlMapping.ADMIN_GUIDE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminGuideController {
    private final GuideService guideService;

    @GetMapping(UrlMapping.GET_ALL_GUIDES)
    @Operation(summary = "Lấy danh sách tất cả hướng dẫn viên")
    ResponseEntity<ApiResponse<List<GuideResponse>>> getAllGuides() {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Lấy danh sách tất cả hướng dẫn viên thành công", guideService.getAllGuides())
        );
    }


    @GetMapping(UrlMapping.GET_GUIDE_BY_ID)
    @Operation(summary = "Lấy thông tin hướng dẫn viên theo ID")
    public ResponseEntity<ApiResponse<GuideResponse>> getGuideById(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy thông tin thành công", guideService.getGuideById(id)));
    }

    @PostMapping(UrlMapping.CREATE_GUIDE)
    @Operation(summary = "Thêm hướng dẫn viên")
    public ResponseEntity<ApiResponse<GuideResponse>> createGuide(@RequestBody GuideRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Thêm thành công", guideService.createGuide(request)));
    }

    @PutMapping(UrlMapping.UPDATE_GUIDE)
    @Operation(summary = "Cập nhật thông tin hướng dẫn viên")
    public ResponseEntity<ApiResponse<GuideResponse>> updateGuide(@PathVariable String id, @RequestBody GuideRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật thông tin thành công", guideService.updateGuide(id, request)));
    }

    @PatchMapping(UrlMapping.UPDATE_STATUS_GUIDE)
    @Operation(summary = "Cập nhật trạng thái hướng dẫn viên")
    public ResponseEntity<ApiResponse<GuideResponse>> updateStatusGuide(
            @PathVariable String id,
            @RequestBody UpdateStatusGuideRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật trạng thái thành công", guideService.updateStatus(id, request)));
    }

}
