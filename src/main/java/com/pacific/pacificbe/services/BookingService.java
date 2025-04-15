package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.BookingRequest;
import com.pacific.pacificbe.dto.request.CancelBookingRequest;
import com.pacific.pacificbe.dto.response.BookingResponse;
import com.pacific.pacificbe.dto.response.BookingStatusStats;
import com.pacific.pacificbe.dto.response.YearlyRevenueOverviewDTO;
import com.pacific.pacificbe.dto.response.report.BookingRevenueReportDTO;
import com.pacific.pacificbe.dto.response.report.Revenue;
import com.pacific.pacificbe.dto.response.report.TourAndBookReport;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    List<Revenue> getMonthlyRevenueReport(String years, String bookingStatus);

    List<Revenue> getYearlyRevenueReport(String bookingStatus);

    List<BookingRevenueReportDTO> getTourBookingsRevenueReport(String tourId, String bookingStatus, LocalDate startDate, LocalDate endDate);

    YearlyRevenueOverviewDTO getYearlyRevenueOverview();

    List<TourAndBookReport> getTourAndBookingsReport(String tourId, String userName);

    List<BookingResponse> getAllBookings();

    List<BookingResponse> getAllByUser(String bookingNo, String status,
                                       LocalDateTime startDate, LocalDateTime endDate,
                                       String tourDetailId, String paymentMethod,
                                       BigDecimal minAmount, BigDecimal maxAmount);

    BookingResponse getBookingById(String bookingId);

    BookingResponse getBookingByBookingNo(String bookingNo);

    BookingResponse bookingTour(String tourDetailId, BookingRequest request);

    BookingResponse cancelBookingFromUser(String bookingId, CancelBookingRequest request);

    BookingResponse cancelBookingFromAdmin(String bookingId, CancelBookingRequest request);

    List<BookingStatusStats> getBookingStatusStats();

    List<BookingResponse> getAllByStatus(String status);
}
