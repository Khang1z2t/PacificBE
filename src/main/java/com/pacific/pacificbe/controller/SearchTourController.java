package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.response.TourResponse;
import com.pacific.pacificbe.services.TourService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/search-tours")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchTourController {

    TourService tourService;

    @GetMapping
    @Operation(summary = "Tìm kiếm tour theo bộ lọc: nơi đến, ngày đi, ngày về, giá tiền")
    public ResponseEntity<ApiResponse<List<TourResponse>>> searchTours(
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {

//        List<TourResponse> result = tourService.searchTours(destination, departureDate, returnDate, minPrice, maxPrice);
        return ResponseEntity.ok(
                ApiResponse.<List<TourResponse>>builder()
                        .data(null)
                        .build()
        );
    }
}
