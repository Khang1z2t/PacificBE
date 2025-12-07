package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.HotelRequest;
import com.pacific.pacificbe.dto.response.HotelResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.HotelMapper;
import com.pacific.pacificbe.model.Hotel;
import com.pacific.pacificbe.repository.HotelRepository;
import com.pacific.pacificbe.services.GoogleDriveService;
import com.pacific.pacificbe.services.HotelService;
import com.pacific.pacificbe.services.SupabaseService;
import com.pacific.pacificbe.utils.enums.FolderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    private final GoogleDriveService googleDriveService;
    private final SupabaseService supabaseService;

    @Override
    public List<HotelResponse> getAllHotels() {
        var hotels = hotelRepository.findAll();
        return hotelMapper.toResponseList(hotels);
    }

    @Override
    public HotelResponse getHotelById(String id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.HOTEL_NOT_FOUND));
        return hotelMapper.toResponse(hotel);
    }

    @Override
    public HotelResponse createHotel(HotelRequest request, MultipartFile image) {
        Hotel hotel = new Hotel();
        return getHotelResponse(request, image, hotel);
    }

    @Override
    public HotelResponse updateHotel(String id, HotelRequest request, MultipartFile image) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.HOTEL_NOT_FOUND));
        return getHotelResponse(request, image, hotel);
    }

    private HotelResponse getHotelResponse(HotelRequest request, MultipartFile image, Hotel hotel) {
        hotel.setName(request.getName());
        hotel.setRating(request.getRating());
        hotel.setCost(request.getCost());
        hotel.setTypeHotel(request.getTypeHotel());
        if (image != null && !image.isEmpty()) {
            hotel.setImageURL(uploadImage(image));
        }
        hotel = hotelRepository.save(hotel);
        return hotelMapper.toResponse(hotel);
    }


    @Override
    public List<HotelResponse> searchHotels(String name, BigDecimal minPrice, BigDecimal maxPrice, String typeHotel) {
        return hotelRepository.findByNameContainingAndCostBetweenAndTypeHotel(
                        name,
                        minPrice != null ? minPrice : BigDecimal.ZERO,
                        maxPrice != null ? maxPrice : BigDecimal.valueOf(Double.MAX_VALUE),
                        typeHotel
                ).stream()
                .map(hotelMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteHotel(String id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.HOTEL_NOT_FOUND));
        String hotelImage = hotel.getImageURL();
        if (hotelImage != null && !hotelImage.isEmpty()) {
            supabaseService.deleteImage(hotelImage);
        }
        hotelRepository.delete(hotel);
    }

    private String uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_IMAGE);
        }
        try {
            return supabaseService.uploadImage(file, FolderType.HOTEL, true);
        } catch (Exception e) {
            throw new AppException(ErrorCode.UPLOAD_IMAGE_FAILED);
        }
    }
}