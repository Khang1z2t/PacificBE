package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.response.WishlistResponse;
import com.pacific.pacificbe.model.Wishlist;

import java.util.List;

public interface WishlistService {
    WishlistResponse addWishlist(String id);
    List<WishlistResponse> getAllWishlistByUser();
    Boolean deleteWishlist(String id);
}
