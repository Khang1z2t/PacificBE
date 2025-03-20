package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.response.report.BookingRevenueReportDTO;
import com.pacific.pacificbe.dto.response.report.Revenue;
import com.pacific.pacificbe.dto.response.report.TourAndBookReport;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    List<Revenue> getMonthlyRevenueReport(String years, String bookingStatus);
    List<Revenue> getYearlyRevenueReport(String bookingStatus);
    List<BookingRevenueReportDTO> getTourBookingsRevenueReport(String tourId,String bookingStatus, LocalDate startDate, LocalDate endDate);
    List<TourAndBookReport> getTourAndBookingsReport(String tourId, String userName);
}
