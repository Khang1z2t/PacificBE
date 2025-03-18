package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.response.HotelResponse;
import com.pacific.pacificbe.mapper.HotelMapper;
import com.pacific.pacificbe.model.Hotel;
import com.pacific.pacificbe.repository.HotelRepository;
import com.pacific.pacificbe.services.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    @Override
    public List<HotelResponse> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(hotelMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public HotelResponse getHotelById(String id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
        return hotelMapper.toResponse(hotel);
    }

    @Override
    public HotelResponse createHotel(Hotel hotel) {
        return null;
    }

    @Override
    public HotelResponse updateHotel(String id, Hotel hotel) {
        return null;
    }

    @Override
    public List<HotelResponse> searchCategories(String name, BigDecimal price, int typeHotel) {
        return List.of();
    }

    @Override
    public void deleteHotel(String id) {

    }


}
