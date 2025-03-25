package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.request.BookingRequest;
import com.pacific.pacificbe.dto.response.BookingResponse;
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

    @GetMapping(UrlMapping.GET_ALL_BOOKINGS)
    @Operation(summary = "Lấy danh sách tất cả đơn đặt tour (yêu cầu token admin)")
    public ResponseEntity<List<BookingRevenueReportDTO>> getAllBookings() {
        return null;
    }

    @GetMapping(UrlMapping.GET_BOOKING_BY_USER)
    @Operation(summary = "Lấy danh sách đơn đặt tour của người dùng (yêu cầu token)")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getBookingByUser() {
        return null;
    }

    @PostMapping(UrlMapping.BOOK_TOUR)
    @Operation(summary = "Đặt tour")
    public ResponseEntity<ApiResponse<BookingResponse>> bookTour(@PathVariable String id, @RequestBody BookingRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(200, null, bookingService.bookingTour(id, request)));
    }
}
