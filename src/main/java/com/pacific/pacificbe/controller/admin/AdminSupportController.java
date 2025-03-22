package com.pacific.pacificbe.controller.admin;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.request.SupportRequest;
import com.pacific.pacificbe.dto.request.UpdateStatusSupportRequest;
import com.pacific.pacificbe.dto.response.SupportResponse;
import com.pacific.pacificbe.repository.UserRepository;
import com.pacific.pacificbe.services.SupportService;
import com.pacific.pacificbe.utils.JavaMail;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping(UrlMapping.ADMIN_SUPPORT)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminSupportController {
    SupportService supportService;

    @GetMapping(UrlMapping.GET_ALL_SUPPORTS)
    @Operation(summary = "Lấy danh sách tất cả email cần hỗ trợ")
    ResponseEntity<ApiResponse<List<SupportResponse>>> getAllSupports() {
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy danh sách thành công", supportService.getAllSupports()));
    }


    @GetMapping(UrlMapping.GET_SUPPORT_BY_ID)
    @Operation(summary = "Lấy thông tin theo ID")
    public ResponseEntity<ApiResponse<SupportResponse>> getSupportById(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy thông tin thành công", supportService.getSupportById(id)));
    }


    @PatchMapping(UrlMapping.UPDATE_STATUS_SUPPORT)
    @Operation(summary = "Cập nhật trạng thái hỗ trợ")
    public ResponseEntity<ApiResponse<SupportResponse>> updateStatusSupport(
            @PathVariable String id,
            @RequestBody UpdateStatusSupportRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật trạng thái thành công", supportService.updateStatus(id, request)));
    }


    @PostMapping(UrlMapping.SEND_MAIL)
    @Operation(summary = "Gửi mail phản hồi")
    public ResponseEntity<ApiResponse<String>> testMail(@RequestBody SupportRequest request) {
        try {
            return ResponseEntity.ok(new ApiResponse<>(200, "Gửi mail thành công", "OK"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(500, "Lỗi gửi mail: " + e.getMessage(), null));
        }
    }

}
