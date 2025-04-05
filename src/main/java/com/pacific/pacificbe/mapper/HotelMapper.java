package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.request.HotelRequest;
import com.pacific.pacificbe.dto.response.HotelResponse;
import com.pacific.pacificbe.model.Hotel;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class HotelMapper {

    public Hotel toEntity(HotelRequest hotelRequest) {
        Hotel hotel = new Hotel();
        hotel.setName(hotelRequest.getName());
        hotel.setTypeHotel(hotelRequest.getTypeHotel());
        hotel.setCost(hotelRequest.getCost());
        hotel.setRating(hotelRequest.getRating());
        hotel.setImageURL(hotelRequest.getImageURL());
        return hotel;
    }

    public HotelResponse toResponse(Hotel hotel) {
        HotelResponse hotelResponse = new HotelResponse();
        hotelResponse.setId(hotel.getId());
        hotelResponse.setName(hotel.getName());
        hotelResponse.setTypeHotel(hotel.getTypeHotel());
        hotelResponse.setCost(hotel.getCost());
        hotelResponse.setRating(new BigDecimal(hotel.getRating()));
        hotelResponse.setImageURL(hotel.getImageURL());
        return hotelResponse;
    }

}
