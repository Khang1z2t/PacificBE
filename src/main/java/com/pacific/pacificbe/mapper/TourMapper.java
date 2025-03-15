package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.request.CreateTourRequest;
import com.pacific.pacificbe.dto.response.BaseTourResponse;
import com.pacific.pacificbe.dto.response.TourDetailResponse;
import com.pacific.pacificbe.dto.response.TourByIdResponse;
import com.pacific.pacificbe.dto.response.TourResponse;
import com.pacific.pacificbe.model.Tour;
import com.pacific.pacificbe.model.TourDetail;
import com.pacific.pacificbe.utils.IdUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
        return toBaseTourResponse(new TourResponse(), tour);
    }

    public TourByIdResponse toTourByIdResponse(Tour tour) {
        TourByIdResponse tourResponse = toBaseTourResponse(new TourByIdResponse(), tour);
        tourResponse.setDetail(
                tour.getTourDetails().stream()
                        .map(this::toTourDetailResponse)
                        .toList());
        return tourResponse;
    }

    public TourDetailResponse toTourDetailResponse(TourDetail tourDetail) {
        return modelMapper.map(tourDetail, TourDetailResponse.class);
    }

    public List<TourResponse> toTourResponseList(List<Tour> tours) {
        return tours.stream()
                .map(this::toTourResponse)
                .toList();
    }

    public List<TourDetailResponse> toTourDetailResponseList(List<TourDetail> tourDetails) {
        return tourDetails.stream().map(this::toTourDetailResponse).toList();
    }

    private <T extends BaseTourResponse> T toBaseTourResponse(T tourResponse, Tour tour ) {
        modelMapper.map(tour, tourResponse);

        tourResponse.setImages(tour.getImages().stream()
                .map(image -> idUtil.getIdImage(image.getImageUrl()))
                .toList());

        if (tour.getThumbnailUrl() != null) {
            tourResponse.setThumbnail(idUtil.getIdImage(tour.getThumbnailUrl()));
        }
        if (tour.getCategory() != null) {
            tourResponse.setCategory(tour.getCategory().getTitle());
        }
        if (tour.getDestination() != null) {
            tourResponse.setDestination(tour.getDestination().getCountry());
        }

        tourResponse.setRatingAvg(tour.getTourDetails().stream()
                .mapToDouble(TourDetail::getRatingAvg)
                .average()
                .orElse(0.0));

        List<TourDetail> activeTourDetails = tour.getTourDetails().stream()
                .filter(TourDetail::isActive)
                .toList();

        if (!activeTourDetails.isEmpty()) {
            tourResponse.setMinPrice(activeTourDetails.stream()
                    .map(TourDetail::getPriceAdults)
                    .min(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO));
            tourResponse.setMaxPrice(activeTourDetails.stream()
                    .map(TourDetail::getPriceAdults)
                    .max(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO));
        }
        return tourResponse;
    }
}
