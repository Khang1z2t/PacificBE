package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.response.showTour.ItineraryTourDetailResponse;
import com.pacific.pacificbe.repository.ItineraryRepository;
import com.pacific.pacificbe.services.ItineraryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItineraryServiceImpl implements ItineraryService {

    ItineraryRepository itineraryRepository;

    @Override
    public List<ItineraryTourDetailResponse> getItineraryByTourAndDate(String tourId, String createdDay) {
        return itineraryRepository.getItineraryByTourAndDate(tourId, createdDay);
    }
}
