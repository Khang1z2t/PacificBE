package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.CreateTourDetailRequest;
import com.pacific.pacificbe.dto.response.TourDetailResponse;
import com.pacific.pacificbe.mapper.TourDetailMapper;
import com.pacific.pacificbe.mapper.TourMapper;
import com.pacific.pacificbe.model.*;
import com.pacific.pacificbe.repository.*;
import com.pacific.pacificbe.services.TourDetailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TourDetailServiceImpl implements TourDetailService {

    private final TourRepository tourRepository;
    private final ComboRepository comboRepository;
    private final HotelRepository hotelRepository;
    private final TransportRepository transportRepository;
    private final TourDetailRepository tourDetailRepository;
    private final TourDetailMapper tourDetailMapper;
    private final TourMapper tourMapper;

    @Override
    public TourDetailResponse addTourDetail(CreateTourDetailRequest request) {
        Tour tour = tourRepository.findById(request.getTourId()).orElse(null);
        Combo combo = comboRepository.findById(request.getComboId()).orElse(null);
        Hotel hotel = hotelRepository.findById(request.getHotelId()).orElse(null);
        Transport transport = transportRepository.findById(request.getTransportId()).orElse(null);

        TourDetail tourDetail = new TourDetail();
        tourDetail.setTitle(request.getTitle());
        tourDetail.setDescriptions(request.getDescriptions());
        tourDetail.setDuration(request.getDuration());
        tourDetail.setPriceAdults(request.getPriceAdults());
        tourDetail.setPriceChildren(request.getPriceChildren());
        tourDetail.setStartDate(request.getStartDate());
        tourDetail.setEndDate(request.getEndDate());
        tourDetail.setQuantity(request.getQuantity());
        tourDetail.setTour(tour);
        tourDetail.setCombo(combo);
        tourDetail.setHotel(hotel);
        tourDetail.setTransport(transport);

        return null;
    }

    @Override
    public List<TourDetailResponse> getAll() {
        List<TourDetail> tourDetail = tourDetailRepository.findAll();
        return tourMapper.toTourDetailResponseList(tourDetail);
    }
}
