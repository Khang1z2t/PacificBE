//package com.pacific.pacificbe.controller;
//
//import com.pacific.pacificbe.dto.ApiResponse;
//import com.pacific.pacificbe.dto.request.GuideRequest;
//import com.pacific.pacificbe.dto.response.GuideResponse;
//import com.pacific.pacificbe.services.GuideService;
//import com.pacific.pacificbe.utils.UrlMapping;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import java.util.List;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping(UrlMapping.GUIDE)
//@Tag(name = "Guide Controller", description = "Các thao tác CRUD cho guide")
//public class GuideController {
//
//    private final GuideService guideService;
//
//    @Autowired
//    public GuideController(GuideService guideService) {
//        this.guideService = guideService;
//    }
//
//    @Operation(
//            summary = "Tạo mới Guide",
//            description = "Tạo mới một guide và trả về thông tin guide vừa tạo"
//    )
//    @PostMapping(UrlMapping.CREATE_GUIDE)
//    public ResponseEntity<ApiResponse<GuideResponse>> createGuide(@RequestBody GuideRequest request) {
//        GuideResponse response = guideService.createGuide(request);
//        return ResponseEntity.ok(ApiResponse.<GuideResponse>builder().data(response).build());
//    }
//
//    @Operation(
//            summary = "Lấy Guide theo ID",
//            description = "Trả về thông tin chi tiết của guide dựa trên ID"
//    )
//    @GetMapping(UrlMapping.GET_GUIDE_BY_ID)
//    public ResponseEntity<ApiResponse<GuideResponse>> getGuideById(@PathVariable String id) {
//        GuideResponse response = guideService.getGuideById(id);
//        return ResponseEntity.ok(ApiResponse.<GuideResponse>builder().data(response).build());
//    }
//
//    @Operation(
//            summary = "Lấy danh sách Guide",
//            description = "Trả về danh sách tất cả các guide"
//    )
//    @GetMapping(UrlMapping.GET_ALL_GUIDES)
//    public ResponseEntity<ApiResponse<List<GuideResponse>>> getAllGuides() {
//        List<GuideResponse> response = guideService.getAllGuides();
//        return ResponseEntity.ok(ApiResponse.<List<GuideResponse>>builder().data(response).build());
//    }
//
//    @Operation(
//            summary = "Cập nhật Guide",
//            description = "Cập nhật thông tin của guide dựa trên ID"
//    )
//    @PutMapping(UrlMapping.UPDATE_GUIDE)
//    public ResponseEntity<ApiResponse<GuideResponse>> updateGuide(@PathVariable String id,
//                                                                  @RequestBody GuideRequest request) {
//        GuideResponse response = guideService.updateGuide(id, request);
//        return ResponseEntity.ok(ApiResponse.<GuideResponse>builder().data(response).build());
//    }
//}
