package com.pacific.pacificbe.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.DetailRatingStats;
import com.pacific.pacificbe.dto.TopTour;
import com.pacific.pacificbe.dto.response.BookingStatusStats;
import com.pacific.pacificbe.dto.response.RatingStats;
import com.pacific.pacificbe.dto.response.RevenueStats;
import com.pacific.pacificbe.dto.response.YearlyRevenueOverviewDTO;
import com.pacific.pacificbe.dto.response.report.BookingRevenueReportDTO;
import com.pacific.pacificbe.dto.response.report.Revenue;
import com.pacific.pacificbe.dto.response.report.TourAndBookReport;
import com.pacific.pacificbe.dto.response.showTour.TourBookingCount;
import com.pacific.pacificbe.model.Tour;
import com.pacific.pacificbe.services.BookingService;
import com.pacific.pacificbe.services.ExportExcelService;
import com.pacific.pacificbe.services.TourService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
	private final ExportExcelService exportExcelService;

	@GetMapping(UrlMapping.GET_TOUR_BOOKING_COUNT)
	@Operation(summary = "Tìm kiếm số lần đặt tour")
	public ResponseEntity<ApiResponse<List<TourBookingCount>>> searchTourBookingCounts(@RequestParam (required = false) String tourId) {
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


	@GetMapping(UrlMapping.BOOKING_STATUS_STATS)
	@Operation(summary = "Get booking status statistics for piechart")
	public ResponseEntity<ApiResponse<List<BookingStatusStats>>> getBookingStatusStats() {
		List<BookingStatusStats> stats = bookingService.getBookingStatusStats();
		return ResponseEntity.ok(new ApiResponse<>(200, "Success", stats));
	}

	@GetMapping(UrlMapping.GET_STATS)
	@Operation(summary = "Xuất các trạng thái doanh thu")
	public ResponseEntity<ApiResponse<RevenueStats>> getRevenueStats(@RequestParam(required = false, defaultValue = "week") String period) {
		RevenueStats stats = reportservice.getRevenueStats(period);
		return ResponseEntity.ok(new ApiResponse<>(200,"Success", stats));
	}

	@GetMapping(UrlMapping.REVIEW_STATS)
	@Operation(summary = "Xuất data thống kê review")
	public ResponseEntity<ApiResponse<List<RatingStats>>> getRatingDistribution(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		List<RatingStats> distribution = reportservice.getRatingDistribution(startDate, endDate);
		return ResponseEntity.ok(new ApiResponse<>(200, "Success", distribution));
	}

	@GetMapping(UrlMapping.DETAIL_REVIEW_STATS)
	@Operation(summary = "Xuất data thống kê review chi tiết")
	public ResponseEntity<ApiResponse<List<DetailRatingStats>>> getDetailedRatingDistribution(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		List<DetailRatingStats> distribution = reportservice.getDetailedRatingDistribution(startDate, endDate);
		return ResponseEntity.ok(new ApiResponse<>(200, "Success", distribution));
	}

	@GetMapping(UrlMapping.TOP_BOOKED_TOURS)
	@Operation(summary = "Xuất danh sách tour được đặt nhiều nhất")
	public ResponseEntity<ApiResponse<List<TopTour>>> getTopBookedTours(
			@RequestParam(defaultValue = "5") int limit,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		List<TopTour> topTours = reportservice.getTopBookedTours(limit, startDate, endDate);
		return ResponseEntity.ok(new ApiResponse<>(200, "Success", topTours));
	}

	@GetMapping(UrlMapping.GET_BOOKING_YEARLY)
	@Operation(summary = "Lấy tổng doanh thu và doanh thu từng tháng của năm nay so với năm ngoái")
	public ResponseEntity<ApiResponse<YearlyRevenueOverviewDTO>> getYearlyRevenueOverview() {
		YearlyRevenueOverviewDTO overview = bookingService.getYearlyRevenueOverview();
		return ResponseEntity.ok(new ApiResponse<>(200, "Success", overview));
	}

	@GetMapping(UrlMapping.EXPORT_EXCEL)
	@Operation(summary = "xuất báo cáo theo excel")
	public void exportToExcel(HttpServletResponse response,
							  @RequestParam String exportType ) throws IOException {
		List<BookingStatusStats> stats = bookingService.getBookingStatusStats();
		switch (exportType.toLowerCase()) {
			case "booking":
				exportExcelService.exportToExcel(response, stats, "Report");
				break;
			case "tour":
				exportExcelService.exportToExcel(response, stats, "Report");
				break;
			case "user":
				exportExcelService.exportToExcel(response, stats, "Report");
				break;
			default:
				throw new IllegalArgumentException("Loại xuất không được hỗ trợ: " + exportType);
		}
	}

}