package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.request.BookingRequest;
import com.pacific.pacificbe.dto.response.BookingDetailResponse;
import com.pacific.pacificbe.dto.response.BookingResponse;
import com.pacific.pacificbe.dto.response.ReviewResponseBooking;
import com.pacific.pacificbe.model.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookingMapper {
    private final ModelMapper modelMapper;

    public Booking toBooking(BookingRequest request) {
        Booking booking = modelMapper.map(request, Booking.class);
        List<BookingDetail> bookingDetails = request.getBookingDetails()
                .stream()
                .map(detail -> modelMapper.map(detail, BookingDetail.class))
                .collect(Collectors.toList());
        booking.setBookingDetails(bookingDetails);
        return booking;
    }

    public BookingResponse toBookingResponse(Booking booking) {
        BookingResponse bookingResponse = modelMapper.map(booking, BookingResponse.class);

        if (booking.getTourDetail() != null) {
            bookingResponse.setUserId(booking.getTourDetail().getId());
        }

        if (booking.getUser() != null) {
            bookingResponse.setUserId(booking.getUser().getId());
        }

        if (booking.getVoucher() != null) {
            bookingResponse.setVoucherId(booking.getVoucher().getId());
        }

        List<BookingDetailResponse> detailResponses = booking.getBookingDetails().stream()
                .map(detail -> modelMapper.map(detail, BookingDetailResponse.class))
                .collect(Collectors.toList());
        bookingResponse.setDetails(detailResponses);

        if (booking.getReview() != null) {
            ReviewResponseBooking reviewResponse = modelMapper.map(booking.getReview(), ReviewResponseBooking.class);
            bookingResponse.setReview(reviewResponse);
        }

        return bookingResponse;
    }

    public List<BookingResponse> toBookingResponses(List<Booking> bookings) {
        return bookings.stream()
                .map(this::toBookingResponse)
                .toList();
    }
}
