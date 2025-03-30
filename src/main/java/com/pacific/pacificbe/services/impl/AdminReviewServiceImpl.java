package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.AdminReviewRequest;
import com.pacific.pacificbe.dto.response.AdminReviewResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.AdminReviewMapper;
import com.pacific.pacificbe.model.Booking;
import com.pacific.pacificbe.model.Review;
import com.pacific.pacificbe.model.Tour;
import com.pacific.pacificbe.model.User;
import com.pacific.pacificbe.repository.BookingRepository;
import com.pacific.pacificbe.repository.ReviewRepository;
import com.pacific.pacificbe.repository.TourRepository;
import com.pacific.pacificbe.repository.UserRepository;
import com.pacific.pacificbe.services.AdminReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminReviewServiceImpl implements AdminReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final TourRepository tourRepository;
    private final AdminReviewMapper adminReviewMapper;

    public AdminReviewServiceImpl(ReviewRepository reviewRepository,
                                  UserRepository userRepository,
                                  BookingRepository bookingRepository,
                                  TourRepository tourRepository,
                                  AdminReviewMapper adminReviewMapper) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.tourRepository = tourRepository;
        this.adminReviewMapper = adminReviewMapper;
    }

    @Override
    public AdminReviewResponse createReview(AdminReviewRequest request) {
        Review review = new Review();
        review.setId(UUID.randomUUID().toString());
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        review.setUser(user);

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));
        review.setBooking(booking);

        Tour tour = tourRepository.findById(request.getTourId())
                .orElseThrow(() -> new AppException(ErrorCode.TOUR_NOT_FOUND));
        review.setTour(tour);

        review = reviewRepository.save(review);

        return adminReviewMapper.toResponse(review);
    }

    @Override
    public AdminReviewResponse getReviewById(String id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TOUR_NOT_FOUND));
        return adminReviewMapper.toResponse(review);
    }

    @Override
    public List<AdminReviewResponse> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(adminReviewMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AdminReviewResponse updateReview(String id, AdminReviewRequest request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TOUR_NOT_FOUND));

        if (request.getRating() != null) {
            review.setRating(request.getRating());
        }
        review.setComment(request.getComment());

        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            review.setUser(user);
        }
        if (request.getBookingId() != null) {
            Booking booking = bookingRepository.findById(request.getBookingId())
                    .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));
            review.setBooking(booking);
        }
        if (request.getTourId() != null) {
            Tour tour = tourRepository.findById(request.getTourId())
                    .orElseThrow(() -> new AppException(ErrorCode.TOUR_NOT_FOUND));
            review.setTour(tour);
        }


        review = reviewRepository.save(review);

        return adminReviewMapper.toResponse(review);
    }

    @Override
    public void deleteReview(String id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TOUR_NOT_FOUND));

        reviewRepository.delete(review);
    }

    @Override
    public void updateReviewStatus(String id, String status) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));

        review.setStatus(status);
        reviewRepository.save(review);
    }

    @Override
    public List<AdminReviewResponse> findByTourId(String tourId) {
        return reviewRepository.findByTourId(tourId).stream()
                .map(adminReviewMapper::toResponse)
                .collect(Collectors.toList());
    }
}