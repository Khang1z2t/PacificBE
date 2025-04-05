package com.pacific.pacificbe.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelRequest {
    private String name;
    private Double rating;
    private BigDecimal cost;
    private String typeHotel;
}