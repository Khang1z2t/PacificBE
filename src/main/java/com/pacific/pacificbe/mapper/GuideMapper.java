package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.response.GuideResponse;
import com.pacific.pacificbe.model.Guide;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GuideMapper {
    ModelMapper modelMapper;

    GuideResponse toGuideResponse(Guide guide) {
        return modelMapper.map(guide, GuideResponse.class);
    }
}
