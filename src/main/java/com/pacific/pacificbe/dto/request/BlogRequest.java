package com.pacific.pacificbe.dto.request;

import com.pacific.pacificbe.utils.enums.BlogStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogRequest {
    private String title;
    private String content;
    private List<String> imageUrls;
    private String author;

    @Builder.Default
    private BlogStatus status = BlogStatus.DRAFT;

    @Builder.Default
    private Boolean active = true;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
