package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.BlogRequest;
import com.pacific.pacificbe.dto.request.UpdateBlogRequest;
import com.pacific.pacificbe.dto.request.UpdateStatusBlogRequest;
import com.pacific.pacificbe.dto.request.blog.BlogCategoryRequest;
import com.pacific.pacificbe.dto.response.blog.BlogCategoryResponse;
import com.pacific.pacificbe.dto.response.blog.BlogResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BlogService {
    BlogResponse createBlog(BlogRequest request, MultipartFile thumbnail);

    BlogResponse getBlogByTitle(String title);

    List<BlogResponse> getAllBlogs();

    BlogResponse updateBlog(String id, BlogRequest request, MultipartFile thumbnail);

    BlogResponse updateStatus(String id, UpdateStatusBlogRequest request);

    void deleteBlog(String id, boolean active);

    BlogResponse updateBlogStatus(String id, UpdateStatusBlogRequest request);

    BlogResponse getBlogById(String id);

    BlogResponse getBlogBySlug(String slug, HttpServletRequest request);

    List<BlogCategoryResponse> getAllBlogCategories();

    BlogCategoryResponse createBlogCategory(BlogCategoryRequest request);

    BlogCategoryResponse updateBlogCategory(String id, BlogCategoryRequest request);

    void deleteBlogCategory(String id, boolean active);

}
