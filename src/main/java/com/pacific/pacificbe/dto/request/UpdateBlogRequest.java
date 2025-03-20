package com.pacific.pacificbe.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateBlogRequest {
    private String title;
    private String content;
    private List<String> imageUrls;
    private String author;
    private String status;
}
