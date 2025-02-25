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
        guide.setExperienceYears(request.getExperienceYears());
        guide.setFirstName(request.getFirst_name());
        guide.setLastName(request.getLast_name());
        guide.setPhone(request.getPhone());
        return guide;
    }

    public GuideResponse toResponse(Guide guide) {
        GuideResponse response = new GuideResponse();
        response.setId(guide.getId());
        response.setAddress(guide.getAddress());
        response.setEmail(guide.getEmail());
        response.setExperienceYears(guide.getExperienceYears());
        response.setFirstName(guide.getFirstName());
        response.setLastName(guide.getLastName());
        response.setPhone(guide.getPhone());
        return response;
    }
}
