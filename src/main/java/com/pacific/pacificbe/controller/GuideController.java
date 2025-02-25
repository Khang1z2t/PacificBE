package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.request.GuideRequest;
import com.pacific.pacificbe.dto.response.GuideResponse;
import com.pacific.pacificbe.services.GuideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/guides")
@Tag(name = "Guide Controller", description = "Guide operations")
public class GuideController {

    private final GuideService guideService;

    @Autowired
    public GuideController(GuideService guideService) {
        this.guideService = guideService;
    }

    @Operation(summary = "Tạo mới Guide", description = "Tạo mới 1 guide và trả về thông tin vừa tạo.")
    @PostMapping
    public ResponseEntity<GuideResponse> createGuide(@RequestBody GuideRequest request) {
        GuideResponse response = guideService.createGuide(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lấy Guide theo ID", description = "Trả về thông tin guide dựa trên ID.")
    @GetMapping("/{id}")
    public ResponseEntity<GuideResponse> getGuideById(@PathVariable String id) {
        GuideResponse response = guideService.getGuideById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lấy danh sách Guide", description = "Trả về danh sách tất cả các guide.")
    @GetMapping
    public ResponseEntity<List<GuideResponse>> getAllGuides() {
        List<GuideResponse> response = guideService.getAllGuides();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cập nhật Guide", description = "Cập nhật thông tin guide dựa trên ID.")
    @PutMapping("/{id}")
    public ResponseEntity<GuideResponse> updateGuide(@PathVariable String id, @RequestBody GuideRequest request) {
        GuideResponse response = guideService.updateGuide(id, request);
        return ResponseEntity.ok(response);
    }
}
