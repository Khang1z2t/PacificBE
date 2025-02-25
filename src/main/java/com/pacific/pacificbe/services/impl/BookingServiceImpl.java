package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.response.BookingRevenueReportDTO;
import com.pacific.pacificbe.dto.response.MonthlyRevenue;
import com.pacific.pacificbe.dto.response.YearlyRevenue;
import com.pacific.pacificbe.repository.BookingRepository;
import com.pacific.pacificbe.services.BookingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    @Override
    public List<MonthlyRevenue> getMonthlyRevenueReport() {
        return bookingRepository.getMonthlyRevenue();
    }

    @Override
    public List<YearlyRevenue> getYearlyRevenueReport() {
        return bookingRepository.getYearlyRevenue();
    }

    @Override
    public List<BookingRevenueReportDTO> getTourBookingsRevenueReport(String tourId, LocalDate startDate, LocalDate endDate) {
//        Date start = parseDate(startDate);
//        Date end = parseDate(endDate);
        return bookingRepository.getTourBookingsRevenue(tourId, startDate, endDate);
    }

//    private Date parseDate(LocalDate localDate) {
//        return (localDate == null) ? null : java.sql.Date.valueOf(localDate);
//    }
}
