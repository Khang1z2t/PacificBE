package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.request.BookingRequest;
import com.pacific.pacificbe.dto.request.CancelBookingRequest;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(UrlMapping.BOOKINGS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {
    private final BookingService bookingService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(UrlMapping.GET_ALL_BOOKINGS)
    @Operation(summary = "Lấy danh sách tất cả đơn đặt tour (yêu cầu token admin)")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getAllBookings() {
        return ResponseEntity.ok(new ApiResponse<>(200, null, bookingService.getAllBookings()));
    }

    @GetMapping(UrlMapping.GET_BOOKING_BY_USER)
    @Operation(summary = "Lấy danh sách đơn đặt tour của người dùng (yêu cầu token)")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getBookingByUser(@RequestParam(required = false) String bookingNo,
                                                                               @RequestParam(required = false) String status,
                                                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                                                                               @RequestParam(required = false) String tourDetailId,
                                                                               @RequestParam(required = false) String paymentMethod,
                                                                               @RequestParam(required = false) BigDecimal minAmount,
                                                                               @RequestParam(required = false) BigDecimal maxAmount
//                                                                               @RequestParam(defaultValue = "createdAt") String sortBy,
//                                                                               @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(200, null, bookingService.getAllByUser(bookingNo, status,
                        startDate, endDate, tourDetailId, paymentMethod, minAmount, maxAmount)));
    }

    @GetMapping(UrlMapping.GET_BOOKING_BY_ID)
    @Operation(summary = "Tìm kiếm theo id (ko khuyến nghị show id)")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingById(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>(200, null, bookingService.getBookingById(id)));
    }

    @GetMapping(UrlMapping.GET_BOOKING_BY_NO)
    @Operation(summary = "lấy danh sách đơn đặt hàng của mọi user")
    public ResponseEntity<ApiResponse<BookingResponse>> getAllBooking(@PathVariable String bookingNo) {
        return ResponseEntity.ok(new ApiResponse<>(200, null, bookingService.getBookingByBookingNo(bookingNo)));
    }

    @PostMapping(UrlMapping.BOOK_TOUR)
    @Operation(summary = "Đặt tour")
    public ResponseEntity<ApiResponse<BookingResponse>> bookTour(@PathVariable String id, @RequestBody BookingRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(200, null, bookingService.bookingTour(id, request)));
    }

    @PostMapping(UrlMapping.CANCEL_BOOKING)
    @Operation(summary = "Hủy đơn đặt tour")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(@PathVariable String id, @RequestBody CancelBookingRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(200, null, bookingService.cancelBookingFromUser(id, request)));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(UrlMapping.CANCEL_BOOKING_ADMIN)
    @Operation(summary = "Hủy đơn đặt tour (admin)")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBookingAdmin(@PathVariable String id, @RequestBody CancelBookingRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(200, null, bookingService.cancelBookingFromAdmin(id, request)));
    }

    @GetMapping(UrlMapping.GET_BOOKING_BY_STATUS)
    @Operation(summary = "Lấy danh sách đơn đặt tour theo trạng thái")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getBookingByStatus(@RequestParam String status) {
        return ResponseEntity.ok(new ApiResponse<>(200, null, bookingService.getAllByStatus(status)));
    }

}
