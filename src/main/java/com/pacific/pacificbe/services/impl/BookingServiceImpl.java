package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.BookingDetailRequest;
import com.pacific.pacificbe.dto.request.BookingRequest;
import com.pacific.pacificbe.dto.response.BookingResponse;
import com.pacific.pacificbe.dto.response.report.BookingRevenueReportDTO;
import com.pacific.pacificbe.dto.response.report.Revenue;
import com.pacific.pacificbe.dto.response.report.TourAndBookReport;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.BookingMapper;
import com.pacific.pacificbe.model.Booking;
import com.pacific.pacificbe.model.BookingDetail;
import com.pacific.pacificbe.model.Voucher;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
    public List<TourAndBookReport> getTourAndBookingsReport(String tourId, String userName) {
        return bookingRepository.getTourAndBooking(tourId, userName);
    }

    @Override
    public List<BookingResponse> getAllBookings() {
        var bookings = bookingRepository.findAll();
        return bookingMapper.toBookingResponses(bookings);
    }

    @Override
    public List<BookingResponse> getAllByUser(String bookingNo, String status,
                                              LocalDateTime startDate, LocalDateTime endDate,
                                              String tourDetailId, String paymentMethod,
                                              BigDecimal minAmount, BigDecimal maxAmount) {
        var userID = AuthUtils.getCurrentUserId();
        var user = userRepository.findById(userID).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));

        String sortBy = "createdAt";
        String sortDirection = "DESC";

        Sort sort = Sort.by(sortDirection.equalsIgnoreCase("ASC") ?
                Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        if (!Arrays.asList("createdAt", "totalAmount", "status").contains(sortBy)) {
            sort = Sort.by(Sort.Direction.DESC, "createdAt");
        }

        List<Booking> bookings = bookingRepository
                .findAllByUser(user,
                        bookingNo, status,
                        startDate, endDate,
                        tourDetailId, paymentMethod,
                        minAmount, maxAmount, sort);
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
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        var tourDetail = tourDetailRepository.findById(tourDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.TOUR_DETAIL_NOT_FOUND));

        String lastBookingNo = bookingRepository.findLatestBookingNoOfToday();

        int adultNum = 0;
        int childrenNum = 0;
        BigDecimal totalPrice = BigDecimal.ZERO;

        // Tạo booking
        Booking booking = new Booking();
        booking.setPaymentMethod(request.getPaymentMethod());
        booking.setSpecialRequests(request.getSpecialRequests());
        booking.setUser(user);
        booking.setTourDetail(tourDetail);
        booking.setActive(true);
        booking.setBookingNo(generatorBookingNo(lastBookingNo));
        booking.setStatus(BookingStatus.PENDING.toString());

        // Xử lý voucher nếu có
        Voucher voucher = null;
        if (request.getVoucherCode() != null && !request.getVoucherCode().isEmpty()) {
            voucher = voucherRepository.findByCodeVoucher(request.getVoucherCode())
                    .orElseThrow(() -> new AppException(ErrorCode.INVALID_VOUCHER));

            // Kiểm tra tính hợp lệ của voucher
            boolean isValid = voucherService.checkVoucher(
                    voucher.getCodeVoucher(),
                    BigDecimal.ZERO, // Sẽ cập nhật totalPrice sau
                    tourDetail.getTour().getId()
            );

            if (!isValid || voucher.getQuantity() <= 0 || voucher.getStatus().equals("INACTIVE")) {
                throw new AppException(ErrorCode.INVALID_VOUCHER);
            }

            // Gán voucher nhưng chưa giảm quantity ngay
            booking.setVoucher(voucher);
        }

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

    private BigDecimal getFinalPrice(BigDecimal totalPrice, Booking booking) {
        if (booking.getVoucher() == null) {
            return totalPrice;
        }
        Voucher voucher = booking.getVoucher();
        BigDecimal discountValue = voucher.getDiscountValue();
        BigDecimal discount = totalPrice.multiply(discountValue).divide(BigDecimal.valueOf(100));
        return totalPrice.subtract(discount);
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

//    private Date parseDate(LocalDate localDate) {
//        return (localDate == null) ? null : java.sql.Date.valueOf(localDate);
//    }

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
}
