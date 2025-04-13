package com.pacific.pacificbe.controller.admin;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.request.SupportReplyRequest;
import com.pacific.pacificbe.dto.request.SupportRequest;
import com.pacific.pacificbe.dto.request.UpdateStatusSupportRequest;
import com.pacific.pacificbe.dto.response.SupportResponse;
import com.pacific.pacificbe.model.Support;
import com.pacific.pacificbe.repository.SupportRepository;
import com.pacific.pacificbe.services.SupportService;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlMapping.ADMIN_SUPPORT)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminSupportController {

    SupportService supportService;
    SupportRepository supportRepository;

    @GetMapping(UrlMapping.GET_ALL_SUPPORTS)
    @Operation(summary = "Lấy danh sách tất cả email cần hỗ trợ")
    public ResponseEntity<ApiResponse<List<SupportResponse>>> getAllSupports() {
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy danh sách thành công", supportService.getAllSupports()));
    }

    @GetMapping(UrlMapping.GET_SUPPORT_BY_ID)
    @Operation(summary = "Lấy thông tin theo ID")
    public ResponseEntity<ApiResponse<SupportResponse>> getSupportById(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy thông tin thành công", supportService.getSupportById(id)));
    }

    @PostMapping(UrlMapping.CREATE_SUPPORT)
    @Operation(summary = "Tạo yêu cầu hỗ trợ")
    public ResponseEntity<ApiResponse<SupportResponse>> createSupport(@RequestBody SupportRequest request) {
        SupportResponse response = supportService.createSupport(request);
        return ResponseEntity.ok(new ApiResponse<>(200, "Tạo yêu cầu thành công", response));
    }

    @PatchMapping(UrlMapping.UPDATE_STATUS_SUPPORT)
    @Operation(summary = "Cập nhật trạng thái hỗ trợ")
    public ResponseEntity<ApiResponse<SupportResponse>> updateStatusSupport(
            @PathVariable String id,
            @RequestBody UpdateStatusSupportRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật trạng thái thành công", supportService.updateStatus(id, request)));
    }

    @PostMapping(UrlMapping.SEND_MAIL)
    @Operation(summary = "Gửi mail")
    public ResponseEntity<ApiResponse<String>> testMail(@RequestBody SupportRequest request) {
        try {
            return ResponseEntity.ok(new ApiResponse<>(200, "Gửi mail thành công", "OK"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(500, "Lỗi gửi mail: " + e.getMessage(), null));
        }
    }

    @PutMapping(UrlMapping.ADMIN_FEEDBACK)
    @Operation(summary = "Admin gửi mail phản hồi")
    public ResponseEntity<ApiResponse<String>> respondToSupport(
            @PathVariable("id") String id,
            @RequestBody SupportReplyRequest replyRequest) {

        // Lấy thông tin từ DB
        Support support = supportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu hỗ trợ"));

        // Cập nhật thông tin phản hồi
        support.setResponseMessage(replyRequest.getResponseMessage());
        support.setSubject(replyRequest.getSubject());
        support.setStatus("resolved");

        // Lưu lại vào DB
        supportRepository.save(support);

        // Gửi email phản hồi
        supportService.sendSupportResponseEmail(support);

        return ResponseEntity.ok(new ApiResponse<>(200, "Phản hồi thành công", "OK"));
    }
}
