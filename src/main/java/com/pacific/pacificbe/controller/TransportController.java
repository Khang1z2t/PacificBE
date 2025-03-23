package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.request.TransportRequest;
import com.pacific.pacificbe.dto.response.TransportResponse;
import com.pacific.pacificbe.services.TransportService;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlMapping.TRANSPORTS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransportController {
    private final TransportService transportService;

    @Operation(summary = "Lấy danh sách phương tiện", description = "Trả về danh sách tất cả các phương tiện")
    @GetMapping(UrlMapping.GET_ALL_TRANSPORTS)
    public ResponseEntity<List<TransportResponse>> getAllTransports() {
        List<TransportResponse> response = transportService.getAllTransports();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lấy phương tiện theo ID", description = "Trả về thông tin của phương tiện dựa trên ID")
    @GetMapping(UrlMapping.GET_TRANSPORT_BY_ID)
    public ResponseEntity<TransportResponse> getTransportById(@PathVariable String id) {
        TransportResponse response = transportService.getTransportById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Thêm phương tiện", description = "Thêm một phương tiện mới")
    @PostMapping(UrlMapping.ADD_TRANSPORT)
    public ResponseEntity<ApiResponse<TransportResponse>> addTransport(@RequestBody TransportRequest request) {
        TransportResponse response = transportService.addTransport(request);
        return ResponseEntity.ok(new ApiResponse<>(200, null, response));
    }

    @Operation(summary = "Cập nhật phương tiện", description = "Cập nhật thông tin của phương tiện dựa trên ID")
    @PutMapping(UrlMapping.UPDATE_TRANSPORT)
    public ResponseEntity<ApiResponse<TransportResponse>> updateTransport(@PathVariable String id, @RequestBody TransportRequest request) {
        TransportResponse response = transportService.updateTransport(id, request);
        return ResponseEntity.ok(new ApiResponse<>(200, null, response));
    }

    @Operation(summary = "Xóa phương tiện", description = "Xóa phương tiện dựa trên ID")
    @DeleteMapping(UrlMapping.DELETE_TRANSPORT)
    public ResponseEntity<ApiResponse<Boolean>> deleteTransport(@PathVariable String id) {
        boolean result = transportService.deleteTransport(id);
        return ResponseEntity.ok(new ApiResponse<>(200, null, result));
    }
}