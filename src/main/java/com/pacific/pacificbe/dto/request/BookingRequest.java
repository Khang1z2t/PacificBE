package com.pacific.pacificbe.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingRequest {
    String paymentMethod;
    String specialRequests;
    BigDecimal totalAmount;
    String voucherCode;

    List<BookingDetailRequest> bookingDetails;
}
