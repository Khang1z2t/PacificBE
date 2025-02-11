package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.response.TourResponse;
import com.pacific.pacificbe.model.Tour;
import com.pacific.pacificbe.model.TourImage;
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

    public TourResponse toTourResponse(Tour tour) {
        TourResponse tourResponse = modelMapper.map(tour, TourResponse.class);
        List<String> imageUrls = tour.getTourImages().stream()
                .map(TourImage::getImageUrl)
                .collect(Collectors.toList());
        tourResponse.setTourImages(imageUrls);
        return tourResponse;    }


}
