package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.response.WishlistResponse;
import com.pacific.pacificbe.model.Wishlist;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WishlistMapper {
    ModelMapper modelMapper;

    public WishlistResponse toWishlistResponse(Wishlist wishlist) {
        return modelMapper.map(wishlist, WishlistResponse.class);
    }

    public Wishlist toWishlist(WishlistResponse wishlistResponse) {
        return modelMapper.map(wishlistResponse, Wishlist.class);
    }

    public List<WishlistResponse> toWishlistResponseList(List<Wishlist> wishlists) {
        return wishlists.stream().map(this::toWishlistResponse).toList();
    }
}
