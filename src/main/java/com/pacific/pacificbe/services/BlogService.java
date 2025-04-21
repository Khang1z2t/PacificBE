package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.BlogRequest;
import com.pacific.pacificbe.dto.request.UpdateBlogRequest;
import com.pacific.pacificbe.dto.request.UpdateStatusBlogRequest;
import com.pacific.pacificbe.dto.response.blog.BlogCategoryResponse;
import com.pacific.pacificbe.dto.response.blog.BlogResponse;

import java.util.List;

public interface BlogService {
    BlogResponse createBlog(BlogRequest request);

    BlogResponse getBlogByTitle(String title);

    List<BlogResponse> getAllBlogs();

    BlogResponse updateBlog(String id, UpdateBlogRequest request);

    BlogResponse updateStatus(String id, UpdateStatusBlogRequest request);

    void deleteBlog(String id);

    BlogResponse updateBlogStatus(String id, UpdateStatusBlogRequest request);

    BlogResponse getBlogById(String id);

    BlogResponse getBlogBySlug(String slug);

    List<BlogCategoryResponse> getAllBlogCategories();

}
