package com.pacific.pacificbe.dto.response.blog;

import com.pacific.pacificbe.dto.response.UserResponse;
import com.pacific.pacificbe.dto.response.tour.TourSimpleResponse;
import com.pacific.pacificbe.dto.response.user.UserSimpleResponse;
import com.pacific.pacificbe.utils.enums.BlogStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogResponse {
    String id;
    String title;
    String content;
    BlogStatus status;
    String slug;
    String metaTitle;
    String metaDescription;
    Integer viewCount;
    Integer likeCount;
    UserSimpleResponse user;
    BlogCategorySimpleResponse category;
    List<TourSimpleResponse> tours;

    Boolean active;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
