package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.DetailRatingStats;
import com.pacific.pacificbe.dto.TopTour;
import com.pacific.pacificbe.dto.response.RatingStats;
import com.pacific.pacificbe.dto.response.RevenueStats;
import com.pacific.pacificbe.dto.response.booking.BookingResponse;
import com.pacific.pacificbe.dto.response.user.UserVipResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReportService {

    RevenueStats getRevenueStats(String period);

    public List<RatingStats> getRatingDistribution(LocalDate startDate, LocalDate endDate);

    List<DetailRatingStats> getDetailedRatingDistribution(LocalDate startDate, LocalDate endDate); // API mới

    public List<TopTour> getTopBookedTours(int limit, LocalDate startDate, LocalDate endDate);

    List<BookingResponse> getAllByBookStatus(LocalDate startDate, LocalDate endDate);

    List<UserVipResponse> getAllUserVip();
}
