package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.GuideRequest;
import com.pacific.pacificbe.dto.response.GuideResponse;
import com.pacific.pacificbe.mapper.GuideMapper;
import com.pacific.pacificbe.model.Guide;
import com.pacific.pacificbe.repository.GuideRepository;
import com.pacific.pacificbe.services.GuideService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GuideServiceImpl implements GuideService {

    private final GuideRepository guideRepository;
    private final GuideMapper guideMapper;

    @Autowired
    public GuideServiceImpl(GuideRepository guideRepository, GuideMapper guideMapper) {
        this.guideRepository = guideRepository;
        this.guideMapper = guideMapper;
    }

    @Override
    public GuideResponse createGuide(GuideRequest request) {
        Guide guide = guideMapper.toEntity(request);
        guide.setId(UUID.randomUUID().toString());
        guide = guideRepository.save(guide);
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
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guide not found"));
        guide.setAddress(request.getAddress());
        guide.setEmail(request.getEmail());
        guide.setExperienceYears(request.getExperienceYears());
        guide.setFirstName(request.getFirst_name());
        guide.setLastName(request.getLast_name());
        guide.setPhone(request.getPhone());
        guide = guideRepository.save(guide);
        return guideMapper.toResponse(guide);
    }
}
