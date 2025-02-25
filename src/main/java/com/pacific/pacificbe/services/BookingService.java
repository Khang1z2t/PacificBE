package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.response.BookingRevenueReportDTO;
import com.pacific.pacificbe.dto.response.MonthlyRevenue;
import com.pacific.pacificbe.dto.response.YearlyRevenue;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    List<MonthlyRevenue> getMonthlyRevenueReport();
    List<YearlyRevenue> getYearlyRevenueReport();
    List<BookingRevenueReportDTO> getTourBookingsRevenueReport(String tourId, LocalDate startDate, LocalDate endDate);
}
