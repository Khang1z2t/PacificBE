package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.CreateItineraryRequest;
import com.pacific.pacificbe.dto.request.CreateTourDetailRequest;
import com.pacific.pacificbe.dto.response.TourDetailResponse;
import com.pacific.pacificbe.dto.response.showTour.DetailTourResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.TourDetailMapper;
import com.pacific.pacificbe.mapper.TourMapper;
import com.pacific.pacificbe.model.*;
import com.pacific.pacificbe.repository.*;
import com.pacific.pacificbe.services.TourDetailService;
import com.pacific.pacificbe.utils.enums.TourDetailStatus;
import com.pacific.pacificbe.utils.enums.TourStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TourDetailServiceImpl implements TourDetailService {

    private final TourRepository tourRepository;
    private final HotelRepository hotelRepository;
    private final TransportRepository transportRepository;
    private final TourDetailRepository tourDetailRepository;
    private final TourMapper tourMapper;
    private final GuideRepository guideRepository;

    @Override
    @Transactional
    public TourDetailResponse addTourDetail(CreateTourDetailRequest request) {
        Tour tour = tourRepository.findById(request.getTourId()).orElseThrow(
                () -> new AppException(ErrorCode.TOUR_NOT_FOUND));

        TourDetail tourDetail = new TourDetail();
        tourDetail.setPriceAdults(request.getPriceAdults());
        tourDetail.setPriceChildren(request.getPriceChildren());
        LocalDateTime startDate = LocalDateTime.of(
                request.getStartDate(), request.getStartTime());
        tourDetail.setStartDate(startDate);
        tourDetail.setEndDate(request.getEndDate());
        tourDetail.setQuantity(request.getQuantity());
        tourDetail.setTour(tour);
        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new AppException(ErrorCode.HOTEL_NOT_FOUND));
        tourDetail.setHotel(hotel);
        Transport transport = transportRepository.findById(request.getTransportId())
                .orElseThrow(() -> new AppException(ErrorCode.TRANSPORT_NOT_FOUND));
        Guide guide = guideRepository.findById(request.getGuideId())
                .orElseThrow(() -> new AppException(ErrorCode.GUIDE_NOT_FOUND));
        tourDetail.setGuide(guide);
        tourDetail.setHotel(hotel);
        tourDetail.setTransport(transport);
        tourDetail.setActive(true);
        tourDetail = tourDetailRepository.save(tourDetail);

//        for (CreateItineraryRequest itineraryRequest : request.getItineraries()) {
//            Itinerary itinerary = new Itinerary();
//            itinerary.setDayDetail(itineraryRequest.getDayDetail());
//            itinerary.setTitle(itineraryRequest.getTitle());
//            itinerary.setDayDetail(itineraryRequest.getDayDetail());
//            itinerary.setTour(tourDetail);
//            tourDetail.getItineraries().add(itinerary);
//            itineraryRepository.save(itinerary);
//        }
        tourDetail.setStatus(TourDetailStatus.OPEN.toString());
        tourDetailRepository.save(tourDetail);
        tour.setStatus(TourStatus.PUBLISHED.toString());
        tourRepository.save(tour);
        return tourMapper.toTourDetailResponse(tourDetail);
    }

    @Cacheable(value = "allTourDetails")
    @Override
    public List<TourDetailResponse> getAll() {
        List<TourDetail> tourDetail = tourDetailRepository.findAll();
        return tourMapper.toTourDetailResponseList(tourDetail);
    }

    @Override
    public TourDetailResponse getTourDetailById(String id) {
        TourDetail tourDetail = tourDetailRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.TOUR_DETAIL_NOT_FOUND));
        return tourMapper.toTourDetailResponse(tourDetail);
    }

    @Override
    public List<TourDetailResponse> getTourDetailByTourId(String tourId) {
        List<TourDetail> tourDetail = tourDetailRepository.findByTourId(tourId);
        return tourMapper.toTourDetailResponseList(tourDetail);
    }

    @Override
    public List<DetailTourResponse> getTourDetailMonth(String tourId) {
        return tourDetailRepository.getTourDetailMonth(tourId);
    }

    @Override
    public List<DetailTourResponse> getTourDetailDay(String tourId, String months) {
        return tourDetailRepository.getTourDetailDay(tourId, months);
    }
}