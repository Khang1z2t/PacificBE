package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.request.CreateTourDetailRequest;
import com.pacific.pacificbe.dto.response.TourDetailResponse;
import com.pacific.pacificbe.model.TourDetail;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TourDetailMapper {
    ModelMapper modelMapper;



    public TourDetail toTourDetail(CreateTourDetailRequest createTourDetailRequest) {
        return modelMapper.map(createTourDetailRequest, TourDetail.class);
    }

    public TourDetail toTourDetail(TourDetailResponse tourDetailResponse) {
        return modelMapper.map(tourDetailResponse, TourDetail.class);
    }


}
