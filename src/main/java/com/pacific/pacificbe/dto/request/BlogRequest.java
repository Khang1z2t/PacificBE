package com.pacific.pacificbe.dto.request;

import com.pacific.pacificbe.utils.enums.BlogStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogRequest {
    private String title;
    private String content;
    private String metaTitle;
    private String metaDescription;

    private BlogStatus status;
    private String categoryId;
    private List<String> tourId;
}
