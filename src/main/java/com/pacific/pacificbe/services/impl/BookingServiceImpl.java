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
import com.pacific.pacificbe.model.BookingDetailRepository;
import com.pacific.pacificbe.model.Voucher;
import com.pacific.pacificbe.repository.BookingRepository;
import com.pacific.pacificbe.repository.TourDetailRepository;
import com.pacific.pacificbe.repository.UserRepository;
import com.pacific.pacificbe.repository.VoucherRepository;
import com.pacific.pacificbe.services.BookingService;
import com.pacific.pacificbe.utils.AuthUtils;
import com.pacific.pacificbe.utils.IdUtil;
import com.pacific.pacificbe.utils.enums.AgeGroup;
import com.pacific.pacificbe.utils.enums.BookingStatus;
import com.pacific.pacificbe.utils.enums.GenderEnums;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
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
        return List.of();
    }

    @Override
    public List<BookingResponse> getAllByUser() {
        return List.of();
    }

    @Override
    public BookingResponse getBookingById(String bookingId) {
        return null;
    }

    @Override
    @Transactional
    public BookingResponse bookingTour(String tourDetailId, BookingRequest request) {
        String userId = AuthUtils.getCurrentUserId();
        var user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));

        var tourDetail = tourDetailRepository.findById(tourDetailId).orElseThrow(
                () -> new AppException(ErrorCode.TOUR_DETAIL_NOT_FOUND));

        String lastBookingNo = bookingRepository.findLatestBookingNoOfToday();

        int adultNum = 0;
        int childrenNum = 0;

        Booking booking = new Booking();
        booking.setPaymentMethod(request.getPaymentMethod());
        booking.setSpecialRequests(request.getSpecialRequests());
        booking.setTotalAmount(request.getTotalAmount());
        booking.setUser(user);
        booking.setTourDetail(tourDetail);
        booking.setActive(true);
        booking.setBookingNo(generatorBookingNo(lastBookingNo));
        booking.setStatus(BookingStatus.PENDING.toString());
        if (request.getVoucherId() != null) {
            var voucher = voucherRepository.findById(request.getVoucherId()).orElseThrow(
                    () -> new AppException(ErrorCode.INVALID_VOUCHER));
            booking.setVoucher(voucher);
        }
        bookingRepository.save(booking);

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
        bookingRepository.save(booking);
        return bookingMapper.toBookingResponse(booking);
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
