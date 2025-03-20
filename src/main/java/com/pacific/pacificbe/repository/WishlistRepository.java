package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, String> {

}
