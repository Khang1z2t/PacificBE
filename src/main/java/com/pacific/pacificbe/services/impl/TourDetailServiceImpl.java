package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.CreateTourDetailRequest;
import com.pacific.pacificbe.dto.request.UpdateTourDetailRequest;
import com.pacific.pacificbe.dto.response.tour.TourDetailResponse;
import com.pacific.pacificbe.dto.response.showTour.DetailTourResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.TourMapper;
import com.pacific.pacificbe.model.*;
import com.pacific.pacificbe.repository.*;
import com.pacific.pacificbe.services.TourDetailService;
import com.pacific.pacificbe.utils.enums.BookingStatus;
import com.pacific.pacificbe.utils.enums.TourDetailStatus;
import com.pacific.pacificbe.utils.enums.TourStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
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
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public TourDetailResponse addTourDetail(CreateTourDetailRequest request) {
        Tour tour = tourRepository.findById(request.getTourId()).orElseThrow(
                () -> new AppException(ErrorCode.TOUR_NOT_FOUND));

        TourDetail tourDetail = new TourDetail();
        tourDetail.setPriceAdults(request.getPriceAdults());
        tourDetail.setPriceChildren(request.getPriceChildren());
        tourDetail.setStartDate(request.getStartDate());
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

        tourDetail.setStatus(TourDetailStatus.OPEN.toString());
        tourDetailRepository.save(tourDetail);
        tour.setStatus(TourStatus.PUBLISHED.toString());
        tourRepository.save(tour);
        return tourMapper.toTourDetailResponse(tourDetail);
    }

    @Override
    @Transactional
    public TourDetailResponse updateTourDetail(UpdateTourDetailRequest request, String tourDetailId) {

        TourDetail tourDetail = tourDetailRepository.findById(tourDetailId).orElseThrow(
                () -> new AppException(ErrorCode.TOUR_DETAIL_NOT_FOUND));

//        TourDetail tourDetail = new TourDetail();
        tourDetail.setPriceAdults(request.getPriceAdults());
        tourDetail.setPriceChildren(request.getPriceChildren());
        tourDetail.setStartDate(request.getStartDate());
        tourDetail.setEndDate(request.getEndDate());
        tourDetail.setQuantity(request.getQuantity());

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

        tourDetail.setStatus(TourDetailStatus.OPEN.toString());
        tourDetailRepository.save(tourDetail);

        return tourMapper.toTourDetailResponse(tourDetail);
    }

    @Override
    @Transactional
    @CacheEvict(value = "allTours", allEntries = true)
    public Boolean deleteTourDetail(String id, boolean active) {
        TourDetail tourDetail = tourDetailRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TOUR_DETAIL_NOT_FOUND));
        tourDetail.setActive(active);
        tourDetail.setDeleteAt(LocalDateTime.now());
        if (active) {
            tourDetail.setStatus(TourDetailStatus.OPEN.toString());
        } else {
            tourDetail.setStatus(TourDetailStatus.CLOSED.toString());
        }
        tourDetailRepository.save(tourDetail);
        return true;
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


    @Override
    @Transactional
    public void updateRatingAvg(String tourDetailId) {
        TourDetail tourDetail = tourDetailRepository.findById(tourDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.TOUR_DETAIL_NOT_FOUND));

        List<Review> reviews = reviewRepository.findByTourDetailIdAndBookingStatus(tourDetailId,
                BookingStatus.COMPLETED.toString());

        double ratingAvg = reviews.stream()
                .mapToDouble(review -> review.getRating().doubleValue())
                .average()
                .orElse(0.0);

        tourDetail.setRatingAvg(ratingAvg);
        tourDetailRepository.save(tourDetail);
    }

}