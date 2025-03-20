package com.pacific.pacificbe.repository;

import com.pacific.pacificbe.model.User;
import com.pacific.pacificbe.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, String> {
    List<Wishlist> findAllByUser(User user);

    @Transactional
    @Modifying
    @Query("delete from Wishlist w where w.id = ?1 and w.user = ?2")
    void deleteByIdAndUser(String id, User user);
}
