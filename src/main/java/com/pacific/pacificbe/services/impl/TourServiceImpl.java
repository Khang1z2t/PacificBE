package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.TourRequest;
import com.pacific.pacificbe.dto.response.TourResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.TourMapper;
import com.pacific.pacificbe.model.Category;
import com.pacific.pacificbe.model.Destination;
import com.pacific.pacificbe.model.Guide;
import com.pacific.pacificbe.model.Tour;
import com.pacific.pacificbe.repository.CategoryRepository;
import com.pacific.pacificbe.repository.DestinationRepository;
import com.pacific.pacificbe.repository.GuideRepository;
import com.pacific.pacificbe.repository.TourRepository;
import com.pacific.pacificbe.services.TourService;
import com.pacific.pacificbe.utils.enums.TourStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TourServiceImpl implements TourService {
    TourRepository tourRepository;
    TourMapper tourMapper;
	private final CategoryRepository categoryRepository;
	private final GuideRepository guideRepository;
	private final DestinationRepository destinationRepository;

	@Override
    public List<TourResponse> getAllTours() {
        return tourMapper.toTourResponseList(tourRepository.findAll());
    }

    @Override
    public TourResponse getTourById(String id) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TOUR_NOT_FOUND));
        return tourMapper.toTourResponse(tour);
    }

	@Override
	public TourResponse createTour(TourRequest request) {
		Tour tour = new Tour();
		tour.setTitle(request.getTitle());
		tour.setDescription(request.getDescription());
//		tour.setPriceAdults(request.getPriceAdults());
//		tour.setPriceChildren(request.getPriceChildren());
		tour.setStatus(TourStatus.DRAFT.toString());
		tour.setActive(true);
		if (request.getCategoryId() != null) {
			Category category = categoryRepository.findById(request.getCategoryId())
					.orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
			tour.setCategory(category);
		}

		if (request.getGuideId() != null) {
			 Guide guide = guideRepository.findById(request.getGuideId())
			 		.orElseThrow(() -> new AppException(ErrorCode.GUIDE_NOT_FOUND));
			 tour.setGuide(guide);
		}

		if (request.getDestinationId() != null) {
			Destination destination = destinationRepository.findById(request.getDestinationId())
					.orElseThrow(() -> new AppException(ErrorCode.DESTINATION_NOT_FOUND));
			tour.setDestination(destination);
		}
		tour = tourRepository.save(tour);
		return tourMapper.toTourResponse(tour);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TourResponse> getTourRating(Double rating) {
	    if (rating == null) {
	        throw new IllegalArgumentException("Rating must not be null");
	    }
	    List<Tour> tours = tourRepository.findTourRating(rating);
	    return tourMapper.toTourResponseList(tours);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TourResponse> getTourCategory(String category) {
	    List<Tour> tours = tourRepository.findTourCategory(category);
	    return tourMapper.toTourResponseList(tours);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TourResponse> getTourDestination(String destination) {
	    List<Tour> tours = tourRepository.findTourDestination(destination);
	    return tourMapper.toTourResponseList(tours);
	}

}
