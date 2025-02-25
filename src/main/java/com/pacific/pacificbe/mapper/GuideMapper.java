package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.request.GuideRequest;
import com.pacific.pacificbe.dto.response.GuideResponse;
import com.pacific.pacificbe.model.Guide;
import org.springframework.stereotype.Component;

@Component
public class GuideMapper {

    public Guide toEntity(GuideRequest request) {
        Guide guide = new Guide();
        guide.setAddress(request.getAddress());
        guide.setEmail(request.getEmail());
        guide.setExperience_years(request.getExperience_years());
        guide.setFirst_name(request.getFirst_name());
        guide.setLast_name(request.getLast_name());
        guide.setPhone(request.getPhone());
        return guide;
    }

    public GuideResponse toResponse(Guide guide) {
        GuideResponse response = new GuideResponse();
        response.setId(guide.getId());
        response.setAddress(guide.getAddress());
        response.setEmail(guide.getEmail());
        response.setExperience_years(guide.getExperience_years());
        response.setFirst_name(guide.getFirst_name());
        response.setLast_name(guide.getLast_name());
        response.setPhone(guide.getPhone());
        return response;
    }
}
