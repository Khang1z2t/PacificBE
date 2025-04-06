package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.GuideRequest;
import com.pacific.pacificbe.dto.request.UpdateStatusGuideRequest;
import com.pacific.pacificbe.dto.response.GuideResponse;

import java.util.List;

public interface GuideService {
    GuideResponse createGuide(GuideRequest request);

    GuideResponse getGuideById(String id);

    List<GuideResponse> getAllGuides();

    GuideResponse updateGuide(String id, GuideRequest request);

    GuideResponse updateStatus(String id, boolean active);
}
