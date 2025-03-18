package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.response.showTour.ItineraryTourDetailResponse;
import com.pacific.pacificbe.model.Itinerary;
import com.pacific.pacificbe.repository.ItineraryRepository;
import com.pacific.pacificbe.services.ItineraryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItineraryServiceImpl implements ItineraryService {

    ItineraryRepository itineraryRepository;

    @Override
    public List<Itinerary> getAll() {
        return itineraryRepository.findAll().stream().collect(Collectors.toList());
    }

    @Override
    public List<ItineraryTourDetailResponse> getItineraryByTourAndDate(String tourId, String createdDay) {
        return itineraryRepository.getItineraryByTourAndDate(tourId, createdDay);
    }
}
