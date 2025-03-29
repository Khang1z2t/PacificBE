package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.AddReviewRequest;
import com.pacific.pacificbe.dto.request.ReviewRequest;
import com.pacific.pacificbe.dto.request.UpdateStatusReviewRequest;
import com.pacific.pacificbe.dto.response.ReviewResponse;
import com.pacific.pacificbe.dto.response.ReviewResponseBooking;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.ReviewMapper;
import com.pacific.pacificbe.model.Review;
import com.pacific.pacificbe.model.Tour;
import com.pacific.pacificbe.model.User;
import com.pacific.pacificbe.repository.BookingRepository;
import com.pacific.pacificbe.repository.ReviewRepository;
import com.pacific.pacificbe.repository.TourRepository;
import com.pacific.pacificbe.repository.UserRepository;
import com.pacific.pacificbe.services.ReviewService;
import com.pacific.pacificbe.utils.AuthUtils;
import com.pacific.pacificbe.utils.enums.ReviewStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewServiceImpl implements ReviewService {

    ReviewRepository reviewRepository;
    ReviewMapper reviewMapper;
    UserRepository userRepository;
    TourRepository tourRepository;
    private final BookingRepository bookingRepository;

    @Override
    public ReviewResponse getRatingById(String id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RATING_NOT_FOUND));
        return reviewMapper.toResponse(review);
    }

    @Override
    public List<ReviewResponse> getAllRatings() {
        return reviewRepository.findAll().stream()
                .map(reviewMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRating(String id) {

    }

    @Override
    public void deleteReview(String id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RATING_NOT_FOUND));
        reviewRepository.delete(review);
        log.info("Deleted rating with id: {}", id);
    }

    @Override
    public ReviewResponse createReview(ReviewRequest request) {
        // Lấy User từ database
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Lấy Tour từ database
        Tour tour = tourRepository.findById(request.getTourId())
                .orElseThrow(() -> new AppException(ErrorCode.TOUR_NOT_FOUND));

        // Tạo Review mới
        Review review = new Review();
        review.setComment(request.getComment());
        review.setRating(request.getRating());
        review.setStatus(request.getStatus());
        review.setUser(user);  // Gán User vào review
        review.setTour(tour);  // Gán Tour vào review

        // Lưu Review vào database
        Review savedReview = reviewRepository.save(review);
        log.info("Created review with id: {}", savedReview.getId());

        // Trả về ReviewResponse
        return new ReviewResponse(
                savedReview.getId(),
                savedReview.getStatus(),
                savedReview.getComment(),
                savedReview.getRating(),
                savedReview.getUser().getEmail(),
                savedReview.getTour().getTitle(),
                savedReview.getCreatedAt()
        );
    }

    @Override
    public List<ReviewResponse> getAllReviews() {
        return List.of();
    }

    @Override
    public ReviewResponseBooking createReviewByUser(AddReviewRequest request) {
        String userId = AuthUtils.getCurrentUserId();
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        var tour = tourRepository.findById(request.getTourId()).orElseThrow(
                () -> new AppException(ErrorCode.TOUR_NOT_FOUND));
        var booking = bookingRepository.findById(request.getBookingId()).orElseThrow(
                () -> new AppException(ErrorCode.BOOKING_NOT_FOUND));
        if (!booking.getUser().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.BOOKING_NOT_FOUND);
        }
//        cần đổi khóa ngoại
//        if (!tour.getId().equals(booking.getTourDetail().getId())) {
//            throw new AppException(ErrorCode.TOUR_NOT_FOUND);
//        }

        List<BigDecimal> ratings = Arrays.asList(
                request.getPriceRating(),
                request.getServiceRating(),
                request.getFacilityRating(),
                request.getFoodRating(),
                request.getAccommodationRating()
        );

        List<BigDecimal> validRatings = ratings.stream()
                .filter(Objects::nonNull)
                .toList();

        BigDecimal rating;
        if (validRatings.isEmpty()) {
            rating = BigDecimal.ZERO;
        } else {
            BigDecimal sum = validRatings.stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            rating = sum.divide(BigDecimal.valueOf(validRatings.size()), 1, RoundingMode.HALF_UP);
        }

        Review review = new Review();
        review.setComment(request.getComment());
        review.setPriceRating(request.getPriceRating());
        review.setServiceRating(request.getServiceRating());
        review.setFoodRating(request.getFoodRating());
        review.setFacilityRating(request.getFacilityRating());
        review.setAccommodationRating(request.getAccommodationRating());
        review.setTour(tour);
        review.setUser(user);
        review.setBooking(booking);
        review.setStatus(ReviewStatus.APPROVED.name());
        review.setRating(rating);
        reviewRepository.save(review);

        return reviewMapper.toReviewResponseBooking(review);
    }

    @Override
    @Transactional
    public ReviewResponse updateStatus(String id, UpdateStatusReviewRequest request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RATING_NOT_FOUND));

        String newStatus = request.getStatus();

        if (newStatus != null) {
            try {
                review.setStatus(ReviewStatus.valueOf(newStatus.toUpperCase()).name());
            } catch (IllegalArgumentException e) {
                throw new AppException(ErrorCode.INVALID_RATING_STATUS);
            }
        } else {
            // Toggle between ACTIVE and INACTIVE
            ReviewStatus currentStatus = ReviewStatus.valueOf(review.getStatus().toUpperCase());
            review.setStatus(currentStatus == ReviewStatus.APPROVED ? ReviewStatus.REJECTED.name() : ReviewStatus.APPROVED.name());
        }

        reviewRepository.save(review);
        log.info("Updated rating [{}] to status: {}", id, review.getStatus());
        return reviewMapper.toResponse(review);
    }
}
