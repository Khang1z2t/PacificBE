package com.pacific.pacificbe.dto.response;

import com.pacific.pacificbe.model.User;
import com.pacific.pacificbe.utils.enums.BlogStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogResponse {
    String id;
    String title;
    String content;
    String imageUrl;
    String author;
    BlogStatus status;

    Boolean active;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
