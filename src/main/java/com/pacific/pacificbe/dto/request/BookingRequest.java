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
    Integer adultNum;
    Integer childrenNum;
    Integer totalNumber;
    String paymentMethod;
    String specialRequests;

    String tourDetailId;
    String voucherId;

    List<BookingDetailRequest> bookingDetails;
}
