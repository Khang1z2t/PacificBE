package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.request.CreateTourRequest;
import com.pacific.pacificbe.dto.response.TourDetailResponse;
import com.pacific.pacificbe.dto.response.TourInDetailResponse;
import com.pacific.pacificbe.dto.response.TourResponse;
import com.pacific.pacificbe.model.Image;
import com.pacific.pacificbe.model.Tour;
import com.pacific.pacificbe.model.TourDetail;
import com.pacific.pacificbe.utils.IdUtil;
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
    IdUtil idUtil;

    public Tour toTour(CreateTourRequest request) {
        Tour tour = modelMapper.map(request, Tour.class);
//        tour.setImages(request.getImages().stream()
//                .map(imageUrl -> new Image(null, imageUrl))
//                .collect(Collectors.toList()));
        return tour;
    }

    public TourResponse toTourResponse(Tour tour) {
        TourResponse tourResponse = modelMapper.map(tour, TourResponse.class);
        tourResponse.setImages(tour.getImages().stream()
                .map(image -> idUtil.getIdImage(image.getId()))
                .collect(Collectors.toList()));
        if (tour.getCategory() != null) {
            tourResponse.setCategory(tour.getCategory().getTitle());
        }
        return tourResponse;
    }

    public TourInDetailResponse toTourInDetailResponse(Tour tour) {
        TourInDetailResponse tourResponse = modelMapper.map(tour, TourInDetailResponse.class);
        tourResponse.setImages(tour.getImages().stream()
                .map(image -> idUtil.getIdImage(image.getId()))
                .collect(Collectors.toList()));
        if (tour.getCategory() != null) {
            tourResponse.setCategory(tour.getCategory().getTitle());
        }
        return tourResponse;
    }

    public TourDetailResponse toTourDetailResponse(TourDetail tourDetail) {
        TourDetailResponse tourDetailResponse = modelMapper.map(tourDetail, TourDetailResponse.class);
        TourInDetailResponse tourResponse = this.toTourInDetailResponse(tourDetail.getTour());
        tourDetailResponse.setTour(tourResponse);
        return tourDetailResponse;
    }

    public List<TourResponse> toTourResponseList(List<Tour> tours) {
        return tours.stream()
                .map(this::toTourResponse)
                .toList();
    }

    public List<TourDetailResponse> toTourDetailResponseList(List<TourDetail> tourDetails) {
        return tourDetails.stream().map(this::toTourDetailResponse).toList();
    }


}
