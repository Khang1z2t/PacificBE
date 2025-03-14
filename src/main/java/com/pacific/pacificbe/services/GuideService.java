package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.GuideRequest;
import com.pacific.pacificbe.dto.request.UpdateStatusGuideRequest;
import com.pacific.pacificbe.dto.request.UpdateStatusUserRequest;
import com.pacific.pacificbe.dto.response.GuideResponse;
import com.pacific.pacificbe.dto.response.UserResponse;

import java.util.List;

public interface GuideService {
    GuideResponse createGuide(GuideRequest request);

    GuideResponse getGuideById(String id);

    List<GuideResponse> getAllGuides();

    GuideResponse updateGuide(String id, GuideRequest request);
    // Có thể thêm phương thức deleteGuide nếu cần

    GuideResponse updateStatus(String id, UpdateStatusGuideRequest request);
}