package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.response.WishlistResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.WishlistMapper;
import com.pacific.pacificbe.model.Tour;
import com.pacific.pacificbe.model.User;
import com.pacific.pacificbe.model.Wishlist;
import com.pacific.pacificbe.repository.TourRepository;
import com.pacific.pacificbe.repository.UserRepository;
import com.pacific.pacificbe.repository.WishlistRepository;
import com.pacific.pacificbe.services.WishlistService;
import com.pacific.pacificbe.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final TourRepository tourRepository;
    private final WishlistMapper wishlistMapper;

    @Override
    public WishlistResponse addWishlist(String id) {
        String userId = AuthUtils.getCurrentUserId();
        Wishlist wishlist = new Wishlist();
        Tour tour = tourRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.TOUR_NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
        wishlist.setTour(tour);
        wishlist.setUser(user);
        wishlistRepository.save(wishlist);
        return wishlistMapper.toWishlistResponse(wishlist);
    }

    @Override
    public List<WishlistResponse> getAllWishlistByUser() {
        String userId = AuthUtils.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<Wishlist> wishlists = wishlistRepository.findAllByUser(user);
        return wishlistMapper.toWishlistResponseList(wishlists);
    }

    @Override
    public Boolean deleteWishlist(String id) {
        String userId = AuthUtils.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
        wishlistRepository.deleteByIdAndUser(id, user);
        return true;
    }
}