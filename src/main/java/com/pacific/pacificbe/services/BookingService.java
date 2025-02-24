package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.response.MonthlyRevenue;

import java.util.List;

public interface BookingService {
    List<MonthlyRevenue> getMonthlyRevenueReport();
}
