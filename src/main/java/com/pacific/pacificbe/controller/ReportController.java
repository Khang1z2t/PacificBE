package com.pacific.pacificbe.controller;

import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.response.report.BookingRevenueReportDTO;
import com.pacific.pacificbe.dto.response.report.Revenue;
import com.pacific.pacificbe.dto.response.report.TourAndBookReport;
import com.pacific.pacificbe.dto.response.showTour.TourBookingCount;
import com.pacific.pacificbe.services.BookingService;
import com.pacific.pacificbe.services.TourService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pacific.pacificbe.services.ReportService;
import com.pacific.pacificbe.utils.UrlMapping;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping(UrlMapping.REPORT)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportController {
	private final ReportService reportservice;
	private final TourService tourService;
	private final BookingService bookingService;

	@GetMapping(UrlMapping.GET_TOUR_BOOKING_COUNT)
	@Operation(summary = "Tìm kiếm số lần đặt tour")
	public ResponseEntity<ApiResponse<List<TourBookingCount>>> searchTourBookingCounts(@PathVariable String tourId) {
		return ResponseEntity.ok(new ApiResponse<>(
				200, "Lấy chi tiết tour theo ngày thành công",
				tourService.getTourBookingCounts(tourId)));
	}

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

////	xuất file báo cáo theo pdf
//@GetMapping(UrlMapping.EXPORT_PDF)
//public ResponseEntity<byte[]> exportReport(@RequestParam String reportName) {
//	try {
//		// Lấy dữ liệu từ DB (giả sử có service lấy dữ liệu)
//		List<?> data = List.of(); // Thay bằng dữ liệu thực tế từ database
//
//		// Thêm tham số nếu cần
//		Map<String, Object> parameters = new HashMap();
//		parameters.put("ReportTitle", "Báo cáo sản phẩm");
//
//		byte[] pdfReport = reportservice.exportReport(reportName, data, parameters);
//
//		return ResponseEntity.ok()
//				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + reportName + ".pdf")
//				.contentType(MediaType.APPLICATION_PDF).body(pdfReport);
//	} catch (Exception e) {
//		return ResponseEntity.internalServerError().build();
//	}
//}
//
//@GetMapping("/booking")
//public ResponseEntity<byte[]> exportBookingReport(
//		@RequestParam String year,
//		@RequestParam(required = false) String bookStatus) {
//	try {
//		byte[] report = reportservice.exportBookingReport(year, bookStatus);
//
//		return ResponseEntity.ok()
//				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Booking_Report.pdf")
//				.contentType(MediaType.APPLICATION_PDF)
//				.body(report);
//	} catch (Exception e) {
//		return ResponseEntity.internalServerError().body(("Error: " + e.getMessage()).getBytes());
//	}
//}