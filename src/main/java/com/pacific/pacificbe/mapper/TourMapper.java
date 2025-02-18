package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.response.TourDetailResponse;
import com.pacific.pacificbe.dto.response.TourResponse;
import com.pacific.pacificbe.model.Tour;
import com.pacific.pacificbe.model.TourDetail;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TourMapper {
    ModelMapper modelMapper;
    TourDetailMapper tourDetailMapper;

    public TourResponse toTourResponse(Tour tour) {
        TourResponse tourResponse = modelMapper.map(tour, TourResponse.class);

        Optional<TourDetail> tourDetailOptional = tour.getTourDetails().stream().findFirst();
        if (tourDetailOptional.isPresent()) {
            TourDetail tourDetail = tourDetailOptional.get();
            tourResponse.setItinerary(tourDetail.getItinerary());
            tourResponse.setStartDate(tourDetail.getStartDate().toString());
            tourResponse.setEndDate(tourDetail.getEndDate().toString());
            tourResponse.setDuration(tourDetail.getDuration());
        }
        return tourResponse;
    }

    public List<TourResponse> toTourResponseList(List<Tour> tours) {
        return tours.stream()
                .map(this::toTourResponse)
                .toList();
    }


}
