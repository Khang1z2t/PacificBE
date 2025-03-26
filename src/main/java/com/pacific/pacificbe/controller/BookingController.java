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
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping(UrlMapping.BOOKINGS)
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
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
    private final BookingService bookingService;

    @GetMapping(UrlMapping.REVENUE_BOOKING_MONTH)
    @Operation(summary = "Xuất báo cáo hằng tháng")
    public ResponseEntity<?> getMonthlyRevenue(
            @RequestParam(required = false)
            @Pattern(regexp = "\\d{4}", message = "Năm phải có định dạng YYYY") String years,
            @RequestParam(required = false) String bookingStatus
    ) {
        if (years == null) {
            log.error("Năm không hợp lệ hoặc bị null");
            return ResponseEntity.badRequest().body("Năm không hợp lệ, vui lòng nhập năm hợp lệ (yyyy)");
        }

        List<Revenue> monthlyRevenue = bookingService.getMonthlyRevenueReport(years, bookingStatus);
        return ResponseEntity.ok(monthlyRevenue);
    }

    @GetMapping(UrlMapping.REVENUE_BOOKING_YEAR)
    @Operation(summary = "Xuất báo cáo hằng năm")
    public ResponseEntity<List<Revenue>> getYearlyRevenue(
            @RequestParam(required = false) String bookingStatus
    ) {
        log.info("Lấy báo cáo doanh thu năm với trạng thái: {}", bookingStatus);
        List<Revenue> yearlyRevenue = bookingService.getYearlyRevenueReport(bookingStatus);
        return ResponseEntity.ok(yearlyRevenue);
    }

    @GetMapping(UrlMapping.REVENUE_BOOKING)
    @Operation(summary = "Xuất báo cáo doanh thu tour theo thời gian")
    public ResponseEntity<?> getTourBookingsRevenue(
            @RequestParam(required = false) String tourId,
            @RequestParam(required = false) String bookingStatus,
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        if (startDate.isAfter(endDate)) {
            log.error("Ngày bắt đầu ({}) không thể lớn hơn ngày kết thúc ({})", startDate, endDate);
            return ResponseEntity.badRequest().body("startDate không thể lớn hơn endDate.");
        }

        log.info("Lấy báo cáo doanh thu từ {} đến {}, Tour ID: {}", startDate, endDate, tourId);
        List<BookingRevenueReportDTO> revenue = bookingService.getTourBookingsRevenueReport(tourId, bookingStatus, startDate, endDate);
        return ResponseEntity.ok(revenue);
    }

    @GetMapping(UrlMapping.BOOK_AND_TOUR)
    @Operation(summary = "Xuất báo cáo tour và booking theo tên người dùng và tour")
    public ResponseEntity<List<TourAndBookReport>> getTourAndBookings(
            @RequestParam(required = false) String tourId,
            @RequestParam(required = false) String userName
    ) {
        log.info("Lấy báo cáo booking, Tour ID: {}, Username: {}", tourId, userName);
        List<TourAndBookReport> tourAndBook = bookingService.getTourAndBookingsReport(tourId, userName);
        return ResponseEntity.ok(tourAndBook);
    }

    // Xử lý lỗi chung
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("Lỗi hệ thống: ", e);
        return ResponseEntity.internalServerError().body("Đã xảy ra lỗi, vui lòng thử lại sau.");
    }

    // Xử lý lỗi khi format ngày không hợp lệ
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateParseException(DateTimeParseException e) {
        log.error("Lỗi định dạng ngày: {}", e.getParsedString());
        return ResponseEntity.badRequest().body("Định dạng ngày không hợp lệ, vui lòng sử dụng yyyy-MM-dd.");
    }

    // Xử lý lỗi khi truyền sai kiểu dữ liệu vào API
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Lỗi tham số truyền vào API: {}", e.getMessage());
        return ResponseEntity.badRequest().body("Tham số truyền vào không hợp lệ.");
    }

    // Xử lý lỗi khi tham số không đúng kiểu dữ liệu
    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatchException(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException e) {
        log.error("Lỗi kiểu dữ liệu của tham số: {}", e.getName());
        return ResponseEntity.badRequest().body("Tham số " + e.getName() + " không hợp lệ, vui lòng kiểm tra lại.");
    }
}
