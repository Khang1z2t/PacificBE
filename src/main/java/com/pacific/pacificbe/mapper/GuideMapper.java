package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.request.GuideRequest;
import com.pacific.pacificbe.dto.response.GuideResponse;
import com.pacific.pacificbe.dto.response.UserResponse;
import com.pacific.pacificbe.model.Guide;
import com.pacific.pacificbe.model.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GuideMapper {

    public Guide toEntity(GuideRequest request) {
        return modelMapper.map(request, Guide.class);
    }

    final ModelMapper modelMapper;

    public GuideResponse toResponse(Guide guide) {
        return modelMapper.map(guide, GuideResponse.class);
    }

    public GuideResponse toGuideResponse(Guide guide) {
        return modelMapper.map(guide, GuideResponse.class);
    }

    public List<GuideResponse> toGuideResponseList(List<Guide> guides) {
        return guides.stream()
                .map(this::toGuideResponse)
                .toList();
    }
}

