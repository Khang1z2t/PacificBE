package com.pacific.pacificbe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelResponse {
    private String id;
    private String name;
    private BigDecimal rating;
    private BigDecimal cost;
    private String typeHotel;
    private String imageURL;
}