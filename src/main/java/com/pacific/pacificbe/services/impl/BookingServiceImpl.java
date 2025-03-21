package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.response.BookingResponse;
import com.pacific.pacificbe.dto.response.report.BookingRevenueReportDTO;
import com.pacific.pacificbe.dto.response.report.Revenue;
import com.pacific.pacificbe.dto.response.report.TourAndBookReport;
import com.pacific.pacificbe.model.Booking;
import com.pacific.pacificbe.repository.BookingRepository;
import com.pacific.pacificbe.services.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    @Override
    public List<BookingResponse> getAllBookings() {
        log.info("Fetching all bookings");
        return bookingRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponse getBookingById(Long bookingId) {
        log.info("Fetching booking with ID: {}", bookingId);
        return bookingRepository.findById(String.valueOf(bookingId))
                .map(this::convertToResponse)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));
    }

    @Override
    @Transactional
    public BookingResponse createBooking(BookingResponse bookingRequest) {
        log.info("Creating new booking");
        Booking booking = convertToEntity(bookingRequest);
        booking = bookingRepository.save(booking);
        return convertToResponse(booking);
    }

    @Override
    @Transactional
    public BookingResponse updateBooking(Long bookingId, BookingResponse bookingRequest) {
        log.info("Updating booking with ID: {}", bookingId);
        Booking booking = bookingRepository.findById(String.valueOf(bookingId))
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));

        booking.setTotalAmount(bookingRequest.getTotalAmount());
        booking.setBookingStatus(bookingRequest.getBookingStatus());
        booking.setPaymentMethod(bookingRequest.getPaymentMethod());
        booking = bookingRepository.save(booking);

        return convertToResponse(booking);
    }

    @Override
    @Transactional
    public void deleteBooking(Long bookingId) {
        log.info("Deleting booking with ID: {}", bookingId);
        if (!bookingRepository.existsById(String.valueOf(bookingId))) {
            throw new RuntimeException("Booking not found with ID: " + bookingId);
        }
        bookingRepository.deleteById(String.valueOf(bookingId));
    }

    @Override
    public List<Revenue> getMonthlyRevenueReport(String years, String bookingStatus) {
        log.info("Fetching monthly revenue report for year: {} and status: {}", years, bookingStatus);
        return bookingRepository.getMonthlyRevenue(years, bookingStatus);
    }

    @Override
    public List<Revenue> getYearlyRevenueReport(String bookingStatus) {
        log.info("Fetching yearly revenue report for status: {}", bookingStatus);
        return bookingRepository.getYearlyRevenue(bookingStatus);
    }

    @Override
    public List<BookingRevenueReportDTO> getTourBookingsRevenueReport(String tourId, String bookingStatus, LocalDate startDate, LocalDate endDate) {
        log.info("Fetching tour booking revenue report for tour: {}, status: {}, from: {}, to: {}", tourId, bookingStatus, startDate, endDate);
        return bookingRepository.getTourBookingsRevenue(tourId, bookingStatus, startDate, endDate);
    }

    @Override
    public List<TourAndBookReport> getTourAndBookingsReport(String tourId, String userName) {
        log.info("Fetching tour and booking report for tour: {} and user: {}", tourId, userName);
        return bookingRepository.getTourAndBooking(tourId, userName);
    }

    // Chuyển đổi từ Entity sang DTO
    private BookingResponse convertToResponse(Booking booking) {
        return new BookingResponse(
                Long.parseLong(booking.getId()),
                booking.getTotalAmount(),
                booking.getBookingStatus(),
                booking.getPaymentMethod()
        );
    }

    // Chuyển đổi từ DTO sang Entity
    private Booking convertToEntity(BookingResponse bookingResponse) {
        Booking booking = new Booking();
        booking.setId(String.valueOf(bookingResponse.getId()));
        booking.setTotalAmount(bookingResponse.getTotalAmount());
        booking.setBookingStatus(bookingResponse.getBookingStatus());
        booking.setPaymentMethod(bookingResponse.getPaymentMethod());
        return booking;
    }
}