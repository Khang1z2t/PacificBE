package com.pacific.pacificbe.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RevenueStats {
    long totalBookings;
    double bookingGrowthPercentage;
    BigDecimal totalRevenue;
    double revenueGrowthPercentage;
    long newCustomers;
    double newCustomerGrowthPercentage;
    double cancellationRate;
    double cancellationRateGrowthPercentage;}
