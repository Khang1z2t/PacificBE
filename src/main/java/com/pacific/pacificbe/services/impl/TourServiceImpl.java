package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.CreateTourRequest;
import com.pacific.pacificbe.dto.response.TourResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.TourMapper;
import com.pacific.pacificbe.model.*;
import com.pacific.pacificbe.repository.*;
import com.pacific.pacificbe.services.GoogleDriveService;
import com.pacific.pacificbe.services.TourService;
import com.pacific.pacificbe.utils.enums.FolderType;
import com.pacific.pacificbe.utils.enums.TourStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TourServiceImpl implements TourService {
    TourRepository tourRepository;
    TourMapper tourMapper;
    private final CategoryRepository categoryRepository;
    private final GuideRepository guideRepository;
    private final DestinationRepository destinationRepository;
    private final GoogleDriveService googleDriveService;
    private final ImageRepository imageRepository;

    @Override
    public List<TourResponse> getAllTours() {
        return tourMapper.toTourResponseList(tourRepository.findAll());
    }

    @Override
    public TourResponse getTourById(String id) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TOUR_NOT_FOUND));
        return tourMapper.toTourResponse(tour);
    }

    @Override
    public TourResponse createTour(CreateTourRequest request, MultipartFile thumbnail) {
        Tour tour = new Tour();
        tour.setTitle(request.getTitle());
        tour.setDescription(request.getDescription());
        tour.setDuration(request.getDuration());
        tour.setStatus(TourStatus.DRAFT.toString());
        tour.setActive(true);

        if (thumbnail != null) {
            String thumbnailUrl = googleDriveService.uploadImageToDrive(thumbnail, FolderType.TOUR);
            tour.setThumbnailUrl(thumbnailUrl);
        }

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
            tour.setCategory(category);
        }

        if (request.getGuideId() != null) {
            Guide guide = guideRepository.findById(request.getGuideId())
                    .orElseThrow(() -> new AppException(ErrorCode.GUIDE_NOT_FOUND));
            tour.setGuide(guide);
        }

        if (request.getDestinationId() != null) {
            Destination destination = destinationRepository.findById(request.getDestinationId())
                    .orElseThrow(() -> new AppException(ErrorCode.DESTINATION_NOT_FOUND));
            tour.setDestination(destination);
        }
        tour = tourRepository.save(tour);
        return tourMapper.toTourResponse(tour);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourResponse> getTourRating(Double rating) {
        if (rating == null) {
            throw new IllegalArgumentException("Rating must not be null");
        }
        List<Tour> tours = tourRepository.findTourRating(rating);
        return tourMapper.toTourResponseList(tours);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourResponse> getTourCategory(String category) {
        List<Tour> tours = tourRepository.findTourCategory(category);
        return tourMapper.toTourResponseList(tours);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourResponse> getTourDestination(String destination) {
        List<Tour> tours = tourRepository.findTourDestination(destination);
        return tourMapper.toTourResponseList(tours);
    }

    @Override
    public TourResponse addTourThumbnail(String id, MultipartFile thumbnail) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TOUR_NOT_FOUND));
        String thumbnailUrl = googleDriveService.uploadImageToDrive(thumbnail, FolderType.TOUR);
        tour.setThumbnailUrl(thumbnailUrl);
        return tourMapper.toTourResponse(tour);
    }

    @Override
    public TourResponse addTourImages(String id, MultipartFile[] images) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TOUR_NOT_FOUND));
        for (MultipartFile image : images) {
            String imageUrl = googleDriveService.uploadImageToDrive(image, FolderType.TOUR);
            Image newImage = new Image();
            newImage.setImageUrl(imageUrl);
            newImage.setTour(tour);
            imageRepository.save(newImage);
        }
        return tourMapper.toTourResponse(tour);
    }

}
