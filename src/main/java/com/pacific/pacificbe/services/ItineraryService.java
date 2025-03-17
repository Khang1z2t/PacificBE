package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.response.showTour.ItineraryTourDetailResponse;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItineraryService {
    List<ItineraryTourDetailResponse> getItineraryByTourAndDate(String tourId, String createdDay);
}
