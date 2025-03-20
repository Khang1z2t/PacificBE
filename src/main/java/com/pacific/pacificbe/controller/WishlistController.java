package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.response.WishlistResponse;
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

    @Operation(summary = "Thêm vào danh sách yêu thích (yêu cầu token)", description = "Thêm tour vào danh sách yêu thích")
    @PostMapping(UrlMapping.ADD_WISHLIST)
    public ResponseEntity<ApiResponse<WishlistResponse>> addWishlist(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>(200, null, wishlistService.addWishlist(id)));
    }

    @Operation(summary = "Lấy danh sách yêu thích (yêu cầu token)", description = "Lấy danh sách yêu thích")
    @GetMapping(UrlMapping.GET_ALL_WISHLIST)
    public ResponseEntity<ApiResponse<List<WishlistResponse>>> getAllWishlist() {
        return ResponseEntity.ok(new ApiResponse<>(200, null, wishlistService.getAllWishlistByUser()));
    }

    @DeleteMapping(UrlMapping.DELETE_WISHLIST)
    @Operation(summary = "Xóa tour khỏi danh sách yêu thích (yêu cầu token)")
    public ResponseEntity<ApiResponse<Boolean>> deleteWishlist(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>(200, null, wishlistService.deleteWishlist(id)));
    }
}
