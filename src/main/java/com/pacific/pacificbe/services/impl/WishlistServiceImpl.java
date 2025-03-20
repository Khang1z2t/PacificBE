package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.model.Tour;
import com.pacific.pacificbe.model.User;
import com.pacific.pacificbe.model.Wishlist;
import com.pacific.pacificbe.repository.TourRepository;
import com.pacific.pacificbe.repository.UserRepository;
import com.pacific.pacificbe.repository.WishlistRepository;
import com.pacific.pacificbe.services.WishlistService;
import com.pacific.pacificbe.utils.AuthenUtils;
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

    @Override
    public List<Wishlist> addWishlist(String id) {
        String userId = AuthenUtils.getCurrentUserId();
        Wishlist wishlist = new Wishlist();
        Tour tour = tourRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.TOUR_NOT_FOUND));
        assert userId != null;
        var user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        wishlist.setTour(tour);
        wishlist.setUser(user);
        wishlistRepository.save(wishlist);
        return wishlistRepository.findAll();
    }

    @Override
    public List<Wishlist> getAllWishlist() {
        return wishlistRepository.findAll();
    }

    @Override
    public void deleteWishlist(String id) {
        wishlistRepository.deleteById(id);
    }
}
