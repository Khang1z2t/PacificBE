package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.model.Wishlist;
import com.pacific.pacificbe.services.WishlistService;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlMapping.WISHLIST)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WishlistController {
    private final WishlistService wishlistService;

    @Operation(summary = "Thêm vào danh sách yêu thích", description = "Thêm tour vào danh sách yêu thích")
    @PostMapping(UrlMapping.ADD_WISHLIST)
    public ResponseEntity<List<Wishlist>> addWishlist(@RequestParam String id) {
        List<Wishlist> wl = wishlistService.addWishlist(id);
        return ResponseEntity.ok(wl);
    }

    @Operation(summary = "Lấy danh sách yêu thích", description = "Lấy danh sách yêu thích")
    @GetMapping(UrlMapping.GET_ALL_WISHLIST)
    public ResponseEntity<List<Wishlist>> getAllWishlist() {
        List<Wishlist> wl = wishlistService.getAllWishlist();
        return ResponseEntity.ok(wl);
    }
}
