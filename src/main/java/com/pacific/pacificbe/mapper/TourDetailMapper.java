package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.response.TourDetailResponse;
import com.pacific.pacificbe.model.Image;
import com.pacific.pacificbe.model.TourDetail;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TourDetailMapper {
    ModelMapper modelMapper;

    public TourDetailResponse toTourDetailResponse(TourDetail tourDetail) {
        return modelMapper.map(tourDetail, TourDetailResponse.class);
    }
}
