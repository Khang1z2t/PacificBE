package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.BookingDetailRequest;
import com.pacific.pacificbe.dto.request.BookingRequest;
import com.pacific.pacificbe.dto.request.CancelBookingRequest;
import com.pacific.pacificbe.dto.response.*;
import com.pacific.pacificbe.dto.response.booking.BookingResponse;
import com.pacific.pacificbe.dto.response.booking.BookingStatusStats;
import com.pacific.pacificbe.dto.response.report.BookingRevenueReportDTO;
import com.pacific.pacificbe.dto.response.report.Revenue;
import com.pacific.pacificbe.dto.response.report.TourAndBookReport;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.BookingMapper;
import com.pacific.pacificbe.model.*;
import com.pacific.pacificbe.repository.BookingDetailRepository;
import com.pacific.pacificbe.repository.BookingRepository;
import com.pacific.pacificbe.repository.TourDetailRepository;
import com.pacific.pacificbe.repository.UserRepository;
import com.pacific.pacificbe.repository.VoucherRepository;
import com.pacific.pacificbe.services.BookingService;
import com.pacific.pacificbe.services.VoucherService;
import com.pacific.pacificbe.utils.AuthUtils;
import com.pacific.pacificbe.utils.IdUtil;
import com.pacific.pacificbe.utils.enums.AgeGroup;
import com.pacific.pacificbe.utils.enums.BookingStatus;
import com.pacific.pacificbe.utils.enums.GenderEnums;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserRepository userRepository;
    private final TourDetailRepository tourDetailRepository;
    private final VoucherRepository voucherRepository;
    private final IdUtil idUtil;
    private final BookingDetailRepository bookingDetailRepository;
    private final VoucherService voucherService;

    @Override
    public List<Revenue> getMonthlyRevenueReport(String years, String bookingStatus) {
        return bookingRepository.getMonthlyRevenue(years, bookingStatus);
    }

    @Override
    public List<Revenue> getYearlyRevenueReport(String bookingStatus) {
        return bookingRepository.getYearlyRevenue(bookingStatus);
    }

    @Override
    public List<BookingRevenueReportDTO> getTourBookingsRevenueReport(String tourId, String bookingStatus, LocalDate startDate, LocalDate endDate) {
        return bookingRepository.getTourBookingsRevenue(tourId, bookingStatus, startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public YearlyRevenueOverviewDTO getYearlyRevenueOverview() {
        YearlyRevenueOverviewDTO overview = new YearlyRevenueOverviewDTO();

        // Tổng doanh thu năm nay
        BigDecimal totalRevenue = bookingRepository.getTotalRevenueThisYear();
        overview.setTotalRevenue(totalRevenue != null ? totalRevenue : BigDecimal.ZERO);

        // Tổng doanh thu năm ngoái
        BigDecimal lastYearRevenue = bookingRepository.getTotalRevenueLastYear();
        lastYearRevenue = lastYearRevenue != null ? lastYearRevenue : BigDecimal.ZERO;

        // Chênh lệch doanh thu
        BigDecimal revenueChange = overview.getTotalRevenue().subtract(lastYearRevenue);
        overview.setRevenueChange(revenueChange);

        // Tỷ lệ tăng/giảm
        ChangeDTO change = calculateChange(overview.getTotalRevenue(), lastYearRevenue);
        overview.setChange(change);

        // Doanh thu từng tháng
        List<MonthlyRevenueDTO> monthlyRevenues = initializeMonthlyRevenues();
        List<MonthlyRevenueDTO> dbRevenues = bookingRepository.getMonthlyRevenueThisYear();
        for (MonthlyRevenueDTO dbRevenue : dbRevenues) {
            monthlyRevenues.set(dbRevenue.getMonth() - 1, dbRevenue);
        }
        overview.setMonthlyRevenues(monthlyRevenues);

        return overview;
    }

    private ChangeDTO calculateChange(BigDecimal current, BigDecimal previous) {
        ChangeDTO change = new ChangeDTO();
        if (previous == null || previous.compareTo(BigDecimal.ZERO) == 0) {
            change.setValue(0.0);
            change.setType("neutral");
            return change;
        }
        BigDecimal percentage = current.subtract(previous)
                .divide(previous, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        change.setValue(percentage.doubleValue());
        change.setType(percentage.compareTo(BigDecimal.ZERO) > 0 ? "increase" : "decrease");
        return change;
    }

    private List<MonthlyRevenueDTO> initializeMonthlyRevenues() {
        List<MonthlyRevenueDTO> monthlyRevenues = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            monthlyRevenues.add(new MonthlyRevenueDTO(month, BigDecimal.ZERO));
        }
        return monthlyRevenues;
    }

    @Override
    public List<TourAndBookReport> getTourAndBookingsReport(String tourId, String userName) {
        return bookingRepository.getTourAndBooking(tourId, userName);
    }

    @Override
    public List<BookingResponse> getAllBookings() {
        var bookings = bookingRepository.findAll();
        return bookingMapper.toBookingResponses(bookings);
    }

    @Override
    public List<BookingResponse> getAllByUser(String bookingNo) {
        String userID = AuthUtils.getCurrentUserId();
        List<Booking> bookings = bookingRepository
                .findByUserAndBookingNoOrderByUpdatedAtDesc(userID,
                        bookingNo);
        return bookingMapper.toBookingResponses(bookings);
    }

    @Override
    public BookingResponse getBookingById(String bookingId) {
        var booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new AppException(ErrorCode.BOOKING_NOT_FOUND));
        return bookingMapper.toBookingResponse(booking);
    }

    @Override
    public BookingResponse getBookingByBookingNo(String bookingNo) {
        var booking = bookingRepository.findByBookingNo(bookingNo).orElseThrow(
                () -> new AppException(ErrorCode.BOOKING_NOT_FOUND));
        return bookingMapper.toBookingResponse(booking);
    }

    @Override
    @Transactional
    public BookingResponse bookingTour(String tourDetailId, BookingRequest request) {
        String userId = AuthUtils.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        TourDetail tourDetail = tourDetailRepository.findById(tourDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.TOUR_DETAIL_NOT_FOUND));
        

        String lastBookingNo = bookingRepository.findLatestBookingNoOfToday();

        int adultNum = 0;
        int childrenNum = 0;
        BigDecimal totalPrice = BigDecimal.ZERO;

        // Tạo booking
        Booking booking = new Booking();
        booking.setBookerFullName(request.getBookerFullName());
        booking.setBookerEmail(request.getBookerEmail());
        booking.setBookerPhoneNumber(request.getBookerPhoneNumber());
        booking.setBookerAddress(request.getBookerAddress());
        booking.setPaymentMethod(request.getPaymentMethod());
        booking.setSpecialRequests(request.getSpecialRequests());
        booking.setUser(user);
        booking.setTourDetail(tourDetail);
        booking.setActive(true);
        booking.setBookingNo(generatorBookingNo(lastBookingNo));
        booking.setStatus(BookingStatus.PENDING.toString());

        Hotel hotel = tourDetail.getHotel();
        Transport transport = tourDetail.getTransport();
        totalPrice = totalPrice.add(hotel.getCost());
        totalPrice = totalPrice.add(transport.getCost());
        // Lưu booking trước để có ID
        bookingRepository.save(booking);

        // Xử lý booking details
        List<BookingDetail> bookingDetails = new ArrayList<>();
        for (BookingDetailRequest bookingDetailRequest : request.getBookingDetails()) {
            GenderEnums genderEnums = bookingDetailRequest.getGender();
            if (genderEnums == null) {
                throw new AppException(ErrorCode.INVALID_GENDER);
            }
            AgeGroup ageGroup = bookingDetailRequest.getAgeGroup();
            if (ageGroup == null) {
                throw new AppException(ErrorCode.INVALID_AGE_GROUP);
            }

            totalPrice = totalPrice.add(bookingDetailRequest.getPrice());

            switch (ageGroup) {
                case ADULT -> adultNum++;
                case CHILD -> childrenNum++;
            }

            BookingDetail bookingDetail = getBookingDetail(bookingDetailRequest, booking);
            bookingDetails.add(bookingDetail);
            bookingDetailRepository.save(bookingDetail);
        }

        int totalNumber = adultNum + childrenNum;
        booking.setAdultNum(adultNum);
        booking.setChildrenNum(childrenNum);
        booking.setTotalNumber(totalNumber);
        booking.setBookingDetails(bookingDetails);

        Voucher voucher = null;
        if (request.getVoucherCode() != null && !request.getVoucherCode().isEmpty()) {
            voucher = voucherRepository.findByCodeVoucher(request.getVoucherCode())
                    .orElseThrow(() -> new AppException(ErrorCode.INVALID_VOUCHER));

            // Kiểm tra tính hợp lệ của voucher
            boolean isValid = voucherService.checkVoucher(
                    voucher.getCodeVoucher(),
                    totalPrice,
                    tourDetail.getTour().getId()
            );

            if (!isValid) {
                voucher = null;
            }
        }
        booking.setVoucher(voucher);

        // Cập nhật giá cuối cùng với voucher
        BigDecimal finalTotalPrice = getFinalPrice(totalPrice, booking);
        if (voucher != null && finalTotalPrice.compareTo(voucher.getMinOrderValue()) < 0) {
            throw new AppException(ErrorCode.INVALID_VOUCHER_MIN_ORDER);
        }
        booking.setTotalAmount(finalTotalPrice);

        // Giảm quantity của voucher sau khi xác nhận booking thành công
        if (voucher != null) {
            voucher.setQuantity(voucher.getQuantity() - 1);
            voucherRepository.save(voucher);
        }

        bookingRepository.save(booking);
        return bookingMapper.toBookingResponse(booking);
    }

    @Override
    @Transactional
    public BookingResponse cancelBookingFromUser(String bookingId, CancelBookingRequest request) {
        var userId = AuthUtils.getCurrentUserId();
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        if (!Objects.equals(booking.getUser().getId(), user.getId())) {
            throw new AppException(ErrorCode.BOOKING_NOT_FOUND);
        }

        return cancelBooking(booking, user, request, "User", true);
    }

    @Override
    @Transactional
    public BookingResponse cancelBookingFromAdmin(String bookingId, CancelBookingRequest request) {
        var userId = AuthUtils.getCurrentUserId();
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        return cancelBooking(booking, user, request, "Admin", false);
    }


    @Override
    public List<BookingStatusStats> getBookingStatusStats() {
        List<Object[]> results = bookingRepository.findBookingStatusStats();
        long totalBookings = bookingRepository.bookingCountStats();

        if (totalBookings == 0) {
            return Arrays.stream(BookingStatus.values())
                    .map(status -> new BookingStatusStats(status.toString(), 0L, 0.0))
                    .collect(Collectors.toList());
        }

        Map<String, Long> statusCountMap = results.stream()
                .collect(Collectors.toMap(
                        result -> (String) result[0],
                        result -> (Long) result[1]
                ));

        return Arrays.stream(BookingStatus.values())
                .map(status -> {
                    long count = statusCountMap.getOrDefault(status.toString(), 0L);
                    double percentage = (count * 100.0) / totalBookings;
                    percentage = Math.round(percentage * 100.0) / 100.0;
                    return new BookingStatusStats(status.toString(), count, percentage);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponse> getAllByStatus(String status) {
        List<Booking> bookings = bookingRepository.findByStatus(status);
        return bookingMapper.toBookingResponses(bookings);
    }

    private BigDecimal getFinalPrice(BigDecimal totalPrice, Booking booking) {
        if (booking.getVoucher() == null) {
            return totalPrice;
        }
        Voucher voucher = booking.getVoucher();
        BigDecimal discountValue = voucher.getDiscountValue();
        BigDecimal discount = totalPrice
                .multiply(discountValue)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        if (voucher.getMaxDiscountAmount() != null && discount.compareTo(voucher.getMaxDiscountAmount()) > 0) {
            discount = voucher.getMaxDiscountAmount();
        }
        return totalPrice.subtract(discount).setScale(0, RoundingMode.HALF_UP);
    }

    private static BookingDetail getBookingDetail(BookingDetailRequest bookingDetailRequest, Booking booking) {
        BookingDetail bookingDetail = new BookingDetail();
        bookingDetail.setFullName(bookingDetailRequest.getFullName());
        bookingDetail.setEmail(bookingDetailRequest.getEmail());
        bookingDetail.setPhoneNumber(bookingDetailRequest.getPhoneNumber());
        bookingDetail.setGender(bookingDetailRequest.getGender().toString());
        bookingDetail.setBirthday(bookingDetailRequest.getBirthday());
        bookingDetail.setAgeGroup(bookingDetailRequest.getAgeGroup().toString());
        bookingDetail.setPrice(bookingDetailRequest.getPrice());
        bookingDetail.setBooking(booking);
        return bookingDetail;
    }


    private String generatorBookingNo(String lastBookingNo) {
        Date now = new Date();
        String fullDatePart = new SimpleDateFormat("ddMMyyHHmm").format(now);
        String dayPart = new SimpleDateFormat("ddMMyy").format(now);
        String prefix = "B";
        String chars = prefix + fullDatePart;
        if (lastBookingNo == null || !lastBookingNo.startsWith(prefix + dayPart)) {
            return idUtil.createNewID(chars);
        } else {
            return idUtil.createIDFromLastID(chars, lastBookingNo);
        }
    }

    /**
     * Phương thức chung để xử lý logic hủy booking.
     *
     * @param booking     Booking cần hủy
     * @param user        Người thực hiện hủy
     * @param request     Yêu cầu hủy
     * @param role        Vai trò (User hoặc Admin)
     * @param allowOnHold Nếu true, cho phép set trạng thái ON_HOLD khi có yêu cầu hoàn tiền
     * @return BookingResponse
     */
    private BookingResponse cancelBooking(Booking booking, User user, CancelBookingRequest request, String role, boolean allowOnHold) {

        // Kiểm tra trạng thái booking
        if (!booking.getStatus().equals(BookingStatus.PENDING.toString()) &&
                !booking.getStatus().equals(BookingStatus.PAID.toString())) {
            request.setRefundRequested(false);
        }

        // Tạo thông tin hủy
        String reasonInfo = String.format(
                "[Cancellation] Reason: %s|CancelledBy: %s|DateRequested: %s",
                request.getReason() != null ? request.getReason() : "N/A",
                role + " - " + booking.getUser().getId(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
        );

        // Cập nhật notes
        if (booking.getNotes() != null && !booking.getNotes().isEmpty()) {
            booking.setNotes(reasonInfo);
        } else {
            booking.setNotes(reasonInfo);
        }

        // Set trạng thái
        if (allowOnHold && request.getRefundRequested()) {
            booking.setStatus(BookingStatus.ON_HOLD.toString());
        } else {
            booking.setStatus(BookingStatus.CANCELLED.toString());
        }

        // Lưu booking
        bookingRepository.save(booking);

        // Trả về response
        return bookingMapper.toBookingResponse(booking);
    }

//    TEST AI

}
