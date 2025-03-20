package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.response.report.BookingRevenueReportDTO;
import com.pacific.pacificbe.dto.response.report.Revenue;
import com.pacific.pacificbe.dto.response.report.TourAndBookReport;
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
    public ResponseEntity<List<Revenue>> getMonthlyRevenue(
            @RequestParam(required = true) String years,
            @RequestParam(required = false) String bookingStatus
    ) {
        List<Revenue> monthlyRevenue = bookingService.getMonthlyRevenueReport(years, bookingStatus);
        return ResponseEntity.ok(monthlyRevenue);
    }

    @GetMapping(UrlMapping.REVENUE_BOOKING_YEAR)
    @Operation(summary = "xuất báo cáo hằng năm")
    public ResponseEntity<List<Revenue>> getYearlyRevenue(
            @RequestParam(required = false) String bookingStatus
    ) {
        List<Revenue> yearlyRevenue = bookingService.getYearlyRevenueReport(bookingStatus);
        return ResponseEntity.ok(yearlyRevenue);
    }

    @GetMapping(UrlMapping.REVENUE_BOOKING)
    @Operation(summary = "xuất báo cáo doanh thu tour theo thời gian")
    public ResponseEntity<List<BookingRevenueReportDTO>> getTourBookingsRevenue(
            @RequestParam(required = false) String tourId,
            @RequestParam(required = false) String bookingStatus,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<BookingRevenueReportDTO> revenue = bookingService.getTourBookingsRevenueReport(tourId, bookingStatus, startDate, endDate);
        return ResponseEntity.ok(revenue);
    }

    @GetMapping(UrlMapping.BOOK_AND_TOUR)
    @Operation(summary = "xuất báo cáo tour và booking theo tên người dùng và tour")
    public ResponseEntity<List<TourAndBookReport>> getTourAndBookings(
            @RequestParam(required = false) String tourId,
            @RequestParam(required = false) String userName) {
        List<TourAndBookReport> tourAndBook = bookingService.getTourAndBookingsReport(tourId, userName);
        return ResponseEntity.ok(tourAndBook);
    }
}
