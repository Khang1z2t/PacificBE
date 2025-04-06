package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.CreateTourRequest;
import com.pacific.pacificbe.dto.request.UpdateTourRequest;
import com.pacific.pacificbe.dto.response.TourByIdResponse;
import com.pacific.pacificbe.dto.response.TourResponse;
import com.pacific.pacificbe.dto.response.showTour.TourBookingCount;
import com.pacific.pacificbe.dto.response.showTour.TourDateResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.TourMapper;
import com.pacific.pacificbe.model.*;
import com.pacific.pacificbe.repository.*;
import com.pacific.pacificbe.services.GoogleDriveService;
import com.pacific.pacificbe.services.TourService;
import com.pacific.pacificbe.utils.IdUtil;
import com.pacific.pacificbe.utils.enums.FolderType;
import com.pacific.pacificbe.utils.enums.TourStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TourServiceImpl implements TourService {
    TourRepository tourRepository;
    TourMapper tourMapper;
    private final CategoryRepository categoryRepository;
    private final DestinationRepository destinationRepository;
    private final GoogleDriveService googleDriveService;
    private final ImageRepository imageRepository;
    private final IdUtil idUtil;
    private final TourDetailRepository tourDetailRepository;

    @Cacheable(value = "allTours", key = "#title + '-' + #minPrice + '-' + #maxPrice + '-' + #categoryId + '-' + #startDate + '-' + #endDate")
    @Override
    public List<TourResponse> getAllTours(String title, BigDecimal minPrice, BigDecimal maxPrice, String categoryId, LocalDate startDate, LocalDate endDate) {
        List<Tour> tours = tourRepository.findAllWithFilters(title, minPrice, maxPrice, categoryId, startDate, endDate);
        return tourMapper.toTourResponseList(tours);
    }

    @Override
    public TourByIdResponse getTourById(String id) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TOUR_NOT_FOUND));
        return tourMapper.toTourByIdResponse(tour);
    }

    @Override
    @Transactional
    @CacheEvict(value = "allTours", allEntries = true)
    public TourResponse createTour(CreateTourRequest request, MultipartFile thumbnail, MultipartFile[] images) {
        Tour tour = new Tour();
        tour.setTitle(request.getTitle());
        tour.setDescription(request.getDescription());
        tour.setDuration(request.getDuration());
        tour.setStatus(TourStatus.DRAFT.toString());
        tour.setActive(true);
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
            tour.setCategory(category);
        }

        if (request.getDestinationId() != null) {
            Destination destination = destinationRepository.findById(request.getDestinationId())
                    .orElseThrow(() -> new AppException(ErrorCode.DESTINATION_NOT_FOUND));
            tour.setDestination(destination);
        }
        if (thumbnail != null) {
            String thumbnailUrl = googleDriveService.uploadImageToDrive(thumbnail, FolderType.TOUR);
            tour.setThumbnailUrl(thumbnailUrl);
        }
        tour = tourRepository.save(tour);
        if (images != null) {
            addImagesToTour(images, tour);
        }
        return tourMapper.toTourResponse(tour);
    }

    @Override
    @Transactional
    @CacheEvict(value = "allTours", allEntries = true)
    public TourResponse addTourThumbnail(String id, MultipartFile thumbnail) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TOUR_NOT_FOUND));
        String thumbnailUrl = googleDriveService.uploadImageToDrive(thumbnail, FolderType.TOUR);
        tour.setThumbnailUrl(thumbnailUrl);
        tourRepository.save(tour); // Thêm save để cập nhật DB
        return tourMapper.toTourResponse(tour);
    }

    @Override
    @Transactional
    @CacheEvict(value = "allTours", allEntries = true)
    public TourResponse addTourImages(String id, MultipartFile[] images) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TOUR_NOT_FOUND));
        addImagesToTour(images, tour);
        tourRepository.save(tour); // Thêm save để cập nhật DB
        return tourMapper.toTourResponse(tour);
    }

    @Override
    @Transactional
    @CacheEvict(value = "allTours", allEntries = true)
    public TourResponse updateTour(String id, UpdateTourRequest request, MultipartFile thumbnail, MultipartFile[] images) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TOUR_NOT_FOUND));

        tour.setTitle(request.getTitle());
        tour.setDescription(request.getDescription());
        tour.setDuration(request.getDuration());
        tour.setStatus(request.getStatus());

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        tour.setCategory(category);

        Destination destination = destinationRepository.findById(request.getDestinationId())
                .orElseThrow(() -> new AppException(ErrorCode.DESTINATION_NOT_FOUND));
        tour.setDestination(destination);

        if (thumbnail != null) {
            String thumbnailUrl = googleDriveService.uploadImageToDrive(thumbnail, FolderType.TOUR);
            tour.setThumbnailUrl(thumbnailUrl);
        }

        if (images != null) {
            addImagesToTour(images, tour);
        }

        tour = tourRepository.save(tour);
        return tourMapper.toTourResponse(tour);
    }

    @Override
    @Transactional
    @CacheEvict(value = "allTours", allEntries = true)
    public Boolean deleteTour(String id, boolean active) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TOUR_NOT_FOUND));
        tour.setActive(active);
        tour.setDeleteAt(LocalDateTime.now());
        if (active) {
            tour.setStatus(TourStatus.PUBLISHED.toString());
        } else {
            tour.setStatus(TourStatus.UNAVAILABLE.toString());
        }
        tourRepository.save(tour);
        return true;
    }

    @Override
    @Transactional
    public Boolean deleteTourForce(String id) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TOUR_NOT_FOUND));
        tourRepository.delete(tour);
        return true;
    }

    @Override
    public List<TourDateResponse> getToursByDate(LocalDateTime startDate, LocalDateTime endDate) {
        return tourRepository.findToursByDate(startDate, endDate);
    }

    @Override
    public TourResponse getTourByTourDetailId(String tourDetailId) {
        var tourDetail = tourDetailRepository.findById(tourDetailId).orElseThrow(
                () -> new AppException(ErrorCode.TOUR_DETAIL_NOT_FOUND));
        var tour = tourRepository.findByTourDetails_Id(tourDetail.getId()).orElseThrow(
                () -> new AppException(ErrorCode.TOUR_NOT_FOUND));
        return tourMapper.toTourResponse(tour);
    }

    private void addImagesToTour(MultipartFile[] images, Tour tour) {
        Set<Image> imageSet = new HashSet<>();
        for (MultipartFile image : images) {
            String imageUrl = googleDriveService.uploadImageToDrive(image, FolderType.TOUR_DETAIL);
            String generatedId = idUtil.getIdImage(imageUrl);
            Image newImage = new Image();
            newImage.setId(generatedId != null ? generatedId : idUtil.generateId());
            newImage.setImageUrl(imageUrl);
            newImage.setTour(tour);
            imageSet.add(imageRepository.save(newImage));
        }
        tour.setImages(imageSet);
    }

    @Override
    public List<TourBookingCount> getTourBookingCounts(String tourId) {
        return tourRepository.findTourBookingCounts(tourId);
    }
}