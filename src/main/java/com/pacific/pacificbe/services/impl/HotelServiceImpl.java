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

    @Override
    @Transactional(readOnly = true)
    public List<HotelResponse> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(hotelMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public HotelResponse getHotelById(String id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.HOTEL_NOT_FOUND));
        return hotelMapper.toResponse(hotel);
    }

    @Override
    @Transactional
    public HotelResponse createHotel(HotelRequest request) {
        Hotel hotel = new Hotel();
        hotel.setName(request.getName());
        hotel.setRating(String.valueOf(request.getRating()));
        hotel.setCost(request.getCost());
        hotel.setTypeHotel(request.getTypeHotel());
        hotel.setImageURL(request.getImageURL());

        hotel = hotelRepository.save(hotel);
        return hotelMapper.toResponse(hotel);
    }

    @Override
    @Transactional
    public HotelResponse createHotelWithImage(HotelRequest request, MultipartFile image) {
        String imageUrl = uploadImage(image);

        Hotel hotel = new Hotel();
        hotel.setName(request.getName());
        hotel.setRating(String.valueOf(request.getRating()));
        hotel.setCost(request.getCost());
        hotel.setTypeHotel(request.getTypeHotel());
        hotel.setImageURL(imageUrl);

        hotel = hotelRepository.save(hotel);
        return hotelMapper.toResponse(hotel);
    }

    @Override
    @Transactional
    public HotelResponse updateHotel(String id, HotelRequest request) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.HOTEL_NOT_FOUND));

        hotel.setName(request.getName());
        hotel.setRating(String.valueOf(request.getRating()));
        hotel.setCost(request.getCost());
        hotel.setTypeHotel(request.getTypeHotel());

        if (request.getImageURL() != null) {
            hotel.setImageURL(request.getImageURL());
        }

        hotel = hotelRepository.save(hotel);
        return hotelMapper.toResponse(hotel);
    }

    @Override
    @Transactional
    public HotelResponse updateHotelImage(String id, MultipartFile image) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.HOTEL_NOT_FOUND));

        String imageUrl = uploadImage(image);
        hotel.setImageURL(imageUrl);

        hotel = hotelRepository.save(hotel);
        return hotelMapper.toResponse(hotel);
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional
    public void deleteHotel(String id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.HOTEL_NOT_FOUND));
        hotelRepository.delete(hotel);
    }

    private String uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_IMAGE);
        }
        try {
            return googleDriveService.uploadImageToDrive(file, FolderType.HOTELS);
        } catch (Exception e) {
            throw new AppException(ErrorCode.UPLOAD_IMAGE_FAILED);
        }
    }
}