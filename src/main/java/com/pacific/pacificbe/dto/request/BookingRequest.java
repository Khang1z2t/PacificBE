package com.pacific.pacificbe.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingRequest {
    String paymentMethod;
    String specialRequests;
    String voucherId;

    List<BookingDetailRequest> bookingDetails;
}
