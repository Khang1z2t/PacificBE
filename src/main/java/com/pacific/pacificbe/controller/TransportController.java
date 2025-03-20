package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.response.TransportResponse;
import com.pacific.pacificbe.services.TransportService;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
