package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.HotelRequest;
import com.pacific.pacificbe.dto.response.HotelResponse;
import com.pacific.pacificbe.model.Hotel;

import java.math.BigDecimal;
import java.util.List;

public interface HotelService {
    List<HotelResponse> getAllHotels();
    HotelResponse getHotelById(String id);
    HotelResponse createHotel(Hotel hotel);
    HotelResponse updateHotel(String id, Hotel hotel);
    List<HotelResponse> searchCategories(String name, BigDecimal price, int typeHotel);
    void deleteHotel(String id);

}
