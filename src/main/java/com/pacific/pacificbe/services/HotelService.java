package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.HotelRequest;
import com.pacific.pacificbe.dto.response.HotelResponse;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface HotelService {
    List<HotelResponse> getAllHotels();
    HotelResponse getHotelById(String id);

    HotelResponse createHotel(HotelRequest request);

    HotelResponse createHotelWithImage(HotelRequest request, MultipartFile image);

    HotelResponse updateHotel(String id, HotelRequest request);

    HotelResponse updateHotelImage(String id, MultipartFile image);

    List<HotelResponse> searchHotels(
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String typeHotel
    );

    void deleteHotel(String id);
}