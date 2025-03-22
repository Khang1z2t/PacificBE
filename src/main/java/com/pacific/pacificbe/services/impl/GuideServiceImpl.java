package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.GuideRequest;
import com.pacific.pacificbe.dto.request.UpdateStatusGuideRequest;
import com.pacific.pacificbe.dto.response.GuideResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.GuideMapper;
import com.pacific.pacificbe.model.Guide;
import com.pacific.pacificbe.repository.GuideRepository;
import com.pacific.pacificbe.services.GuideService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GuideServiceImpl implements GuideService {

    private final GuideRepository guideRepository;
    private final GuideMapper guideMapper;

    @Override
    public GuideResponse createGuide(GuideRequest request) {
        Guide guide = guideMapper.toEntity(request);

        // Lưu vào DB
        guide = guideRepository.save(guide);

        // Convert Entity -> DTO
        return guideMapper.toResponse(guide);
    }

    @Override
    public GuideResponse getGuideById(String id) {
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guide not found"));
        return guideMapper.toResponse(guide);
    }

    @Override
    public List<GuideResponse> getAllGuides() {
        return guideRepository.findAll().stream()
                .map(guideMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public GuideResponse updateGuide(String id, GuideRequest request) {
        // Tìm guide cũ
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guide not found"));
        guide.setAddress(request.getAddress());
        guide.setEmail(request.getEmail());
        guide.setExperienceYears(request.getExperienceYears());
        guide.setFirstName(request.getFirstName());
        guide.setLastName(request.getLastName());
        guide.setPhone(request.getPhone());
//        guide.setStatus(request.getStatus());
        // Lưu
        guide = guideRepository.save(guide);

        return guideMapper.toResponse(guide);
    }

    // Nếu cần xóa:
    // @Override
    // public void deleteGuide(String id) {
    //     Guide guide = guideRepository.findById(id)
    //             .orElseThrow(() -> new AppException(ErrorCode.GUIDE_NOT_FOUND));
    //     guideRepository.delete(guide);
    // }

    @Override
    public GuideResponse updateStatus(String id, UpdateStatusGuideRequest request) {
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.GUIDE_NOT_FOUND));

        // Nếu request có status mới, cập nhật theo request
        if (request.getStatus() != null) {
//            guide.setStatus(request.getStatus());
        } else {
            // Nếu không có status, tự động chuyển đổi giữa ACTIVE và INACTIVE
//            guide.setStatus("ACTIVE".equalsIgnoreCase(guide.getStatus()) ? "INACTIVE" : "ACTIVE");
        }

        return guideMapper.toGuideResponse(guideRepository.save(guide));
    }
}