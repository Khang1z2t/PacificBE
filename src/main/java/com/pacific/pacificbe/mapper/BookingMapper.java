//package com.pacific.pacificbe.mapper;
//
//import com.pacific.pacificbe.dto.response.BookingResponse;
//import com.pacific.pacificbe.model.Booking;
//
//import java.math.BigDecimal;
//
//public class BookingMapper {
//    public static BookingResponse toBookingResponse(Booking booking) {
//        return BookingResponse.builder()
//                .bookingId(booking.getId())
//                .userId(booking.getUser() != null ? booking.getUser().getId() : null)
//                .userName(booking.getUser() != null ? booking.getUser().getUsername() : "Unknown")
//                .tourDetailId(booking.getTourDetail() != null ? booking.getTourDetail().getId() : null)
//                .tourName(booking.getTourDetail() != null && booking.getTourDetail().getTour() != null
//                        ? booking.getTourDetail().getTour().getTitle()
//                        : "Unknown")
//                .bookingDate(booking.getCreatedAt())
//                .adultNum(booking.getAdultNum() != null ? booking.getAdultNum() : 0)
//                .childrenNum(booking.getChildrenNum() != null ? booking.getChildrenNum() : 0)
//                .totalNumber(booking.getTotalNumber() != null ? booking.getTotalNumber() : 0)
//                .totalAmount(booking.getTotalAmount() != null ? booking.getTotalAmount() : BigDecimal.ZERO)
//                .paymentMethod(booking.getPaymentMethod() != null ? booking.getPaymentMethod() : "Unknown")
//                .specialRequests(booking.getSpecialRequests() != null ? booking.getSpecialRequests() : "")
//                .voucherId(booking.getVoucher() != null ? booking.getVoucher().getId() : null)
//                .bookingStatus(booking.getBookingStatus() != null ? booking.getBookingStatus() : "pending")
//                .createdAt(booking.getCreatedAt())
//                .updatedAt(booking.getUpdatedAt())
//                .build();
//    }
//}
