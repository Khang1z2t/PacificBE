package com.pacific.pacificbe.dto.response.showTour;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TourBookingCount {
    Integer bookingCount;
    String  tourDetailId;
    String  tourId;
}
