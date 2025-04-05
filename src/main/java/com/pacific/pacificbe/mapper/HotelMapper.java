package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.request.HotelRequest;
import com.pacific.pacificbe.dto.response.HotelResponse;
import com.pacific.pacificbe.model.Hotel;
import com.pacific.pacificbe.utils.IdUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HotelMapper {
    private final ModelMapper modelMapper;
    private final IdUtil idUtil;

    public Hotel toEntity(HotelRequest hotelRequest) {
        Hotel hotel = new Hotel();
        hotel.setName(hotelRequest.getName());
        hotel.setTypeHotel(hotelRequest.getTypeHotel());
        hotel.setCost(hotelRequest.getCost());
//        hotel.setRating(hotelRequest.getRating().toString());
        return hotel;
    }

    public HotelResponse toResponse(Hotel hotel) {
        HotelResponse hotelResponse = modelMapper.map(hotel, HotelResponse.class);
        hotelResponse.setImage(idUtil.getIdImage(hotel.getImageURL()));
        return hotelResponse;
    }

    public List<HotelResponse> toResponseList(List<Hotel> hotels) {
        return hotels.stream()
                .map(this::toResponse)
                .toList();
    }

}
