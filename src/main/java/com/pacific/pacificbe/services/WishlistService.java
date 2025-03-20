package com.pacific.pacificbe.services;

import com.pacific.pacificbe.model.Wishlist;

import java.util.List;

public interface WishlistService {
    List<Wishlist> addWishlist(String id);
    List<Wishlist> getAllWishlist();
    void deleteWishlist(String id);
}
