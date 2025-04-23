package com.pacific.pacificbe.services.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.pacific.pacificbe.dto.DetailRatingStats;
import com.pacific.pacificbe.dto.RatingLevel;
import com.pacific.pacificbe.dto.TopTour;
import com.pacific.pacificbe.dto.response.RatingStats;
import com.pacific.pacificbe.dto.response.RevenueStats;
import com.pacific.pacificbe.dto.response.booking.BookingResponse;
import com.pacific.pacificbe.dto.response.user.UserVipResponse;
import com.pacific.pacificbe.mapper.BookingMapper;
import com.pacific.pacificbe.mapper.UserMapper;
import com.pacific.pacificbe.model.Booking;
import com.pacific.pacificbe.repository.BookingRepository;
import com.pacific.pacificbe.repository.ReviewRepository;
import com.pacific.pacificbe.repository.TourDetailRepository;
import com.pacific.pacificbe.repository.UserRepository;
import com.pacific.pacificbe.utils.enums.BookingStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.pacific.pacificbe.services.ReportService;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportServiceImpl implements ReportService {
    private JdbcTemplate jdbcTemplate;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final TourDetailRepository tourDetailRepository;
    private final ReviewRepository reviewRepository;
    private final BookingMapper bookingMapper;
    private final UserMapper userMapper;


    @Override
    public RevenueStats getRevenueStats(String period) {
// Xác định khoảng thời gian dựa trên period
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfThisPeriod, endOfThisPeriod, startOfLastPeriod, endOfLastPeriod;
        LocalDateTime startOfToday, endOfToday, startOfYesterday, endOfYesterday;
        List<String> revenueStatus = Arrays.asList(
                BookingStatus.PAID.toString(),
                BookingStatus.ON_GOING.toString(),
                BookingStatus.COMPLETED.toString()
        );

        List<String> cancellationStatus = Arrays.asList(
                BookingStatus.EXPIRED.toString(),
                BookingStatus.CANCELLED.toString(),
                BookingStatus.ON_HOLD.toString()
        );

        // Tính thời gian cho hôm nay và hôm qua (dùng cho "Khách mới")
        startOfToday = now.with(LocalTime.MIN);
        endOfToday = now.with(LocalTime.MAX);
        startOfYesterday = startOfToday.minusDays(1);
        endOfYesterday = startOfToday.minusSeconds(1);

        // Tính khoảng thời gian dựa trên period
        switch (period != null ? period.toLowerCase() : "week") {
            case "month":
                startOfThisPeriod = now.withDayOfMonth(1).with(LocalTime.MIN);
                endOfThisPeriod = now.with(LocalTime.MAX);
                startOfLastPeriod = startOfThisPeriod.minusMonths(1);
                endOfLastPeriod = startOfThisPeriod.minusSeconds(1);
                break;
            case "year":
                startOfThisPeriod = now.withDayOfYear(1).with(LocalTime.MIN);
                endOfThisPeriod = now.with(LocalTime.MAX);
                startOfLastPeriod = startOfThisPeriod.minusYears(1);
                endOfLastPeriod = startOfThisPeriod.minusSeconds(1);
                break;
            case "week":
            default:
                startOfThisPeriod = now.with(ChronoField.DAY_OF_WEEK, 1).with(LocalTime.MIN);
                endOfThisPeriod = now.with(LocalTime.MAX);
                startOfLastPeriod = startOfThisPeriod.minusWeeks(1);
                endOfLastPeriod = startOfThisPeriod.minusSeconds(1);
                break;
        }

        // 1. Tổng Booking
        long totalBookingsThisPeriod = bookingRepository.countBookingsInPeriod(startOfThisPeriod, endOfThisPeriod);
        long totalBookingsLastPeriod = bookingRepository.countBookingsInPeriod(startOfLastPeriod, endOfLastPeriod);
        double bookingGrowthPercentage = calculateGrowthPercentage(totalBookingsThisPeriod, totalBookingsLastPeriod);

        // 2. Doanh thu
        BigDecimal revenueThisPeriod = bookingRepository.getRevenueInPeriod(startOfThisPeriod, endOfThisPeriod, revenueStatus);
        BigDecimal revenueLastPeriod = bookingRepository.getRevenueInPeriod(startOfLastPeriod, endOfLastPeriod, revenueStatus);
        double revenueGrowthPercentage = calculateGrowthPercentage(revenueThisPeriod, revenueLastPeriod);

        // 3. Khách mới (luôn tính theo ngày, không phụ thuộc vào period)
        long newCustomersToday = userRepository.countNewUsersToday(startOfToday, endOfToday);
        long newCustomersYesterday = userRepository.countNewUsersYesterday(startOfYesterday, endOfYesterday);
        double newCustomerGrowthPercentage = calculateGrowthPercentage(newCustomersToday, newCustomersYesterday);

        // 4. Tỷ lệ hủy
        long cancelledBookingsThisPeriod = bookingRepository.countCancelledBookingsInPeriod(startOfThisPeriod, endOfThisPeriod, cancellationStatus);
        double cancellationRateThisPeriod = totalBookingsThisPeriod > 0 ? (cancelledBookingsThisPeriod * 100.0) / totalBookingsThisPeriod : 0.0;
        long cancelledBookingsLastPeriod = bookingRepository.countCancelledBookingsInPeriod(startOfLastPeriod, endOfLastPeriod, cancellationStatus);
        double cancellationRateLastPeriod = totalBookingsLastPeriod > 0 ? (cancelledBookingsLastPeriod * 100.0) / totalBookingsLastPeriod : 0.0;
        double cancellationRateGrowthPercentage = calculateGrowthPercentage(cancellationRateThisPeriod, cancellationRateLastPeriod);

        // Làm tròn các giá trị phần trăm
        bookingGrowthPercentage = Math.round(bookingGrowthPercentage * 10.0) / 10.0;
        revenueGrowthPercentage = Math.round(revenueGrowthPercentage * 10.0) / 10.0;
        newCustomerGrowthPercentage = Math.round(newCustomerGrowthPercentage * 10.0) / 10.0;
        cancellationRateThisPeriod = Math.round(cancellationRateThisPeriod * 10.0) / 10.0;
        cancellationRateGrowthPercentage = Math.round(cancellationRateGrowthPercentage * 10.0) / 10.0;

        return new RevenueStats(
                totalBookingsThisPeriod, bookingGrowthPercentage,
                revenueThisPeriod, revenueGrowthPercentage,
                newCustomersToday, newCustomerGrowthPercentage,
                cancellationRateThisPeriod, cancellationRateGrowthPercentage
        );
    }

    private double calculateGrowthPercentage(long current, long previous) {
        if (previous == 0) {
            return current > 0 ? 100.0 : 0.0;
        }
        return ((current - previous) * 100.0) / previous;
    }

    private double calculateGrowthPercentage(BigDecimal current, BigDecimal previous) {
        if (previous == null || previous.compareTo(BigDecimal.ZERO) == 0) {
            return current != null && current.compareTo(BigDecimal.ZERO) > 0 ? 100.0 : 0.0;
        }
        return current.subtract(previous)
                .divide(previous, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }

    private double calculateGrowthPercentage(double current, double previous) {
        if (previous == 0) {
            return current > 0 ? 100.0 : 0.0;
        }
        return ((current - previous) * 100.0) / previous;
    }

    @Override
    public List<RatingStats> getRatingDistribution(LocalDate startDate, LocalDate endDate) {
        // Định nghĩa các khoảng rating
        Map<Integer, float[]> ratingRanges = new LinkedHashMap<>();
        ratingRanges.put(5, new float[]{4.5f, 5.0f});
        ratingRanges.put(4, new float[]{3.5f, 4.4f});
        ratingRanges.put(3, new float[]{2.5f, 3.4f});
        ratingRanges.put(2, new float[]{1.5f, 2.4f});
        ratingRanges.put(1, new float[]{0.0f, 1.4f});

        // Đếm tổng số tour_details
        long totalTourDetails = tourDetailRepository.countTourDetails(startDate, endDate);
        if (totalTourDetails == 0) {
            return ratingRanges.entrySet().stream()
                    .map(entry -> new RatingStats(entry.getKey(), 0, 0.0))
                    .collect(Collectors.toList());
        }

        // Đếm số lượng tour_details cho từng mức rating
        List<RatingStats> distribution = new ArrayList<>();
        for (Map.Entry<Integer, float[]> entry : ratingRanges.entrySet()) {
            int ratingLevel = entry.getKey();
            float minRating = entry.getValue()[0];
            float maxRating = entry.getValue()[1];

            long count = tourDetailRepository.countByRatingRange(minRating, maxRating, startDate, endDate);
            double percentage = (count * 100.0) / totalTourDetails;
            percentage = Math.round(percentage * 10.0) / 10.0; // Làm tròn 1 chữ số thập phân

            distribution.add(new RatingStats(ratingLevel, count, percentage));
        }

        return distribution;
    }

    @Override
    public List<DetailRatingStats> getDetailedRatingDistribution(LocalDate startDate, LocalDate endDate) {
        // Định nghĩa các khoảng rating
        Map<Integer, float[]> ratingRanges = new LinkedHashMap<>();
        ratingRanges.put(5, new float[]{4.5f, 5.0f});
        ratingRanges.put(4, new float[]{3.5f, 4.4f});
        ratingRanges.put(3, new float[]{2.5f, 3.4f});
        ratingRanges.put(2, new float[]{1.5f, 2.4f});
        ratingRanges.put(1, new float[]{0.0f, 1.4f});

        // Chuyển LocalDate thành LocalDateTime
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(LocalTime.MAX) : null;

        // Đếm tổng số review
        long totalReviews = reviewRepository.countReviews(startDateTime, endDateTime);

        // Định nghĩa các loại rating
        Map<String, Function<float[], Long>> ratingTypeFunctions = new LinkedHashMap<>();
        ratingTypeFunctions.put("Accommodation", range -> reviewRepository.countByAccommodationRatingRange(range[0], range[1], startDateTime, endDateTime));
        ratingTypeFunctions.put("Facility", range -> reviewRepository.countByFacilityRatingRange(range[0], range[1], startDateTime, endDateTime));
        ratingTypeFunctions.put("Food", range -> reviewRepository.countByFoodRatingRange(range[0], range[1], startDateTime, endDateTime));
        ratingTypeFunctions.put("Price", range -> reviewRepository.countByPriceRatingRange(range[0], range[1], startDateTime, endDateTime));
        ratingTypeFunctions.put("Service", range -> reviewRepository.countByServiceRatingRange(range[0], range[1], startDateTime, endDateTime));

        List<DetailRatingStats> result = new ArrayList<>();

        // Tính phân bố cho từng loại rating
        for (Map.Entry<String, Function<float[], Long>> ratingTypeEntry : ratingTypeFunctions.entrySet()) {
            String ratingType = ratingTypeEntry.getKey();
            Function<float[], Long> countFunction = ratingTypeEntry.getValue();

            List<RatingLevel> levels = new ArrayList<>();
            if (totalReviews == 0) {
                levels = ratingRanges.entrySet().stream()
                        .map(entry -> new RatingLevel(entry.getKey(), 0, 0.0))
                        .collect(Collectors.toList());
            } else {
                for (Map.Entry<Integer, float[]> rangeEntry : ratingRanges.entrySet()) {
                    int ratingLevel = rangeEntry.getKey();
                    float[] range = rangeEntry.getValue();

                    long count = countFunction.apply(range);
                    double percentage = (count * 100.0) / totalReviews;
                    percentage = Math.round(percentage * 10.0) / 10.0;

                    levels.add(new RatingLevel(ratingLevel, count, percentage));
                }
            }

            result.add(new DetailRatingStats(ratingType, levels));
        }

        return result;
    }

    @Override
    public List<TopTour> getTopBookedTours(int limit, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        Pageable pageable = PageRequest.of(0, limit);
        return bookingRepository.getTopBookedTours(startDateTime, endDateTime, pageable);
    }

    @Override
    public List<BookingResponse> getAllByBookStatus(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Booking> bookings = bookingRepository.findByCreatedAt(startDateTime,endDateTime);
        return bookingMapper.toBookingResponses(bookings);
    }

    @Override
    public List<UserVipResponse> getAllUserVip() {
        return userRepository.findAllUserVip();
    }
}
