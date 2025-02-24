package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.response.MonthlyRevenue;
import com.pacific.pacificbe.services.BookingService;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(UrlMapping.BOOKINGS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {
    private final BookingService bookingService;

    @GetMapping(UrlMapping.REVENUE_BOOKING)
    @Operation(summary = "xuất báo cáo hằng tháng")
    public ResponseEntity<List<MonthlyRevenue>> getMonthlyRevenue() {
        List<MonthlyRevenue> revenue = bookingService.getMonthlyRevenueReport();
        return ResponseEntity.ok(revenue);
    }
}
