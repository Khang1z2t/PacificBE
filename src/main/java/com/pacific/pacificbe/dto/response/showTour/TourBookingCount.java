package com.pacific.pacificbe.dto.response.showTour;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TourBookingCount {
    Integer bookingCount;
    String tourDetailId;
    String tourID;
    String tourTitle;
    Date startDate;
    Date endDate;
    BigDecimal totalAmount;
    String bookingNo;
}
