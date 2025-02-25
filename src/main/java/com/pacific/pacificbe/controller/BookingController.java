package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.response.BookingRevenueReportDTO;
import com.pacific.pacificbe.dto.response.MonthlyRevenue;
import com.pacific.pacificbe.dto.response.YearlyRevenue;
import com.pacific.pacificbe.services.BookingService;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(UrlMapping.BOOKINGS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {
    private final BookingService bookingService;

    @GetMapping(UrlMapping.REVENUE_BOOKING_MONTH)
    @Operation(summary = "xuất báo cáo hằng tháng")
    public ResponseEntity<List<MonthlyRevenue>> getMonthlyRevenue() {
        List<MonthlyRevenue> revenue = bookingService.getMonthlyRevenueReport();
        return ResponseEntity.ok(revenue);
    }

    @GetMapping(UrlMapping.REVENUE_BOOKING_YEAR)
    @Operation(summary = "xuất báo cáo hằng năm")
    public ResponseEntity<List<YearlyRevenue>> getYearlyRevenue() {
        List<YearlyRevenue> revenue = bookingService.getYearlyRevenueReport();
        return ResponseEntity.ok(revenue);
    }

    @GetMapping(UrlMapping.REVENUE_BOOKING)
    @Operation(summary = "xuất báo cáo doanh thu tour theo thời gian")
    public ResponseEntity<List<BookingRevenueReportDTO>> getTourBookingsRevenue(
            @RequestParam(required = false) String tourId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<BookingRevenueReportDTO> revenue = bookingService.getTourBookingsRevenueReport(tourId, startDate, endDate);
        return ResponseEntity.ok(revenue);
    }
}
