package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.request.HotelRequest;
import com.pacific.pacificbe.dto.response.HotelResponse;
import com.pacific.pacificbe.model.Hotel;
import org.springframework.stereotype.Component;

@Component
public class HotelMapper {

    public Hotel toEntity(HotelRequest hotelRequest) {
        Hotel hotel = new Hotel();
        hotel.setName(hotelRequest.getName());
        hotel.setTypeHotel(String.valueOf(hotelRequest.getTypeHotel()));
        hotel.setCost(hotelRequest.getCost());
        return hotel;
    }

    public HotelResponse toResponse(Hotel hotel) {
        HotelResponse hotelResponse = new HotelResponse();
        hotelResponse.setId(hotel.getId());
        hotelResponse.setName(hotel.getName());
        hotelResponse.setTypeHotel(Integer.parseInt(hotel.getTypeHotel()));
        hotelResponse.setCost(hotel.getCost());
        return hotelResponse;
    }

}
