package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.ItineraryDay;
import com.pacific.pacificbe.dto.request.ItineraryRequest;
import com.pacific.pacificbe.dto.response.ItineraryResponse;
import com.pacific.pacificbe.dto.response.showTour.ItineraryTourDetailResponse;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.ItineraryMapper;
import com.pacific.pacificbe.model.Itinerary;
import com.pacific.pacificbe.model.Tour;
import com.pacific.pacificbe.repository.ItineraryRepository;
import com.pacific.pacificbe.repository.TourRepository;
import com.pacific.pacificbe.services.ItineraryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItineraryServiceImpl implements ItineraryService {

    ItineraryMapper itineraryMapper;
    private final ItineraryRepository itineraryRepository;
    private final TourRepository tourRepository;

    @Override
    public List<Itinerary> getAll() {
        return itineraryRepository.findAll().stream().collect(Collectors.toList());
    }

    @Override
    public List<Itinerary> getByTourId(String id) {
        return itineraryRepository.findByTourId(id);
    }

    @Override
    @Transactional
    public List<ItineraryResponse> addItinerary(String tourId, ItineraryRequest request) {
        Tour tour = tourRepository.findById(tourId).orElseThrow(() -> new RuntimeException(ErrorCode.TOUR_NOT_FOUND.toString()));

        List<Itinerary> itineraries = new ArrayList<>();

        for (ItineraryDay day : request.getDays()) {
            if (day.getDayNumber() > tour.getDuration()) {
                throw new RuntimeException("Day number " + day.getDayNumber() + " exceeds tour duration " + tour.getDuration());
            }

            Itinerary itinerary = new Itinerary();
            itinerary.setDayNumber(day.getDayNumber());
            itinerary.setTitle(day.getTitle());
            itinerary.setNotes(day.getNotes());
            itinerary.setTour(tour);
            itinerary.setActive(true);

            itineraries.add(itinerary);
        }
        itineraryRepository.saveAll(itineraries);

        return itineraries.stream().map(itinerary -> {
            ItineraryResponse response = new ItineraryResponse();
            response.setId(itinerary.getId());
            response.setDayNumber(itinerary.getDayNumber());
            response.setTitle(itinerary.getTitle());
            response.setNotes(itinerary.getNotes());
            response.setTourId(tourId);
            response.setActive(itinerary.isActive());
            return response;
        }).toList();
    }

//    @Override
//    public List<ItineraryTourDetailResponse> getItineraryByTourAndDate(String tourId, String createdDay) {
//        return itineraryRepository.getItineraryByTourAndDate(tourId, createdDay);
////        return null;
//    }
}
