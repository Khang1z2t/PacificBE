package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.response.TourResponse;
import com.pacific.pacificbe.model.Image;
import com.pacific.pacificbe.model.Tour;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TourMapper {
    ModelMapper modelMapper;
    TourDetailMapper tourDetailMapper;

    public TourResponse toTourResponse(Tour tour) {
        TourResponse tourResponse = modelMapper.map(tour, TourResponse.class);
        tourResponse.setImages(tour.getImages().stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList()));
        return tourResponse;
    }

    public List<TourResponse> toTourResponseList(List<Tour> tours) {
        return tours.stream()
                .map(this::toTourResponse)
                .toList();
    }


}
