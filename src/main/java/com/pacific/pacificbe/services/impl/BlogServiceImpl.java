package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.BlogRequest;
import com.pacific.pacificbe.dto.request.UpdateBlogRequest;
import com.pacific.pacificbe.dto.request.UpdateStatusBlogRequest;
import com.pacific.pacificbe.dto.response.BlogResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.BlogMapper;
import com.pacific.pacificbe.model.Blog;
import com.pacific.pacificbe.model.Image;
import com.pacific.pacificbe.repository.BlogRepository;
import com.pacific.pacificbe.repository.ImageRepository;
import com.pacific.pacificbe.services.BlogService;
import com.pacific.pacificbe.utils.enums.BlogStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final ImageRepository imageRepository;
    private final BlogMapper blogMapper;

    @Override
    public BlogResponse createBlog(BlogRequest request) {
        if (blogRepository.existsByTitle(request.getTitle())) {
            throw new AppException(ErrorCode.TITLE_NOT_FOUND);
        }

        Blog blog = new Blog();
        blog.setTitle(request.getTitle());
        blog.setContent(request.getContent());
        blog.setStatus(BlogStatus.DRAFT);
        blog.setAuthor(request.getAuthor());

        final Blog savedBlog = blogRepository.save(blog);

        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            List<Image> images = request.getImageUrls().stream().map(url -> {
                Image image = new Image();
                image.setImageUrl(url);
                image.setBlog(savedBlog); // Dùng biến final
                return image;
            }).collect(Collectors.toList());

            imageRepository.saveAll(images);
            savedBlog.setImages(images);
        }

        return blogMapper.toBlogResponse(savedBlog);
    }


    @Override
    public BlogResponse getBlogByTitle(String title) {
        Blog blog = blogRepository.findByTitle(title)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_FOUND));
        return blogMapper.toBlogResponse(blog);
    }

    @Override
    public List<BlogResponse> getAllBlogs() {
        return blogRepository.findAll().stream()
                .map(blogMapper::toBlogResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BlogResponse updateBlog(String id, UpdateBlogRequest request) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_FOUND));

        // Cập nhật thông tin cơ bản
        blog.setTitle(request.getTitle());
        blog.setContent(request.getContent());
        blog.setAuthor(request.getAuthor());
        blog.setStatus(BlogStatus.valueOf(request.getStatus()));

        // Xóa ảnh cũ trong DB
        imageRepository.deleteAll(blog.getImages());
        blog.getImages().clear();

        final Blog savedBlog = blog;

        // Cập nhật danh sách ảnh mới
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            List<Image> images = request.getImageUrls().stream().map(url -> {
                Image image = new Image();
                image.setImageUrl(url);
                image.setBlog(savedBlog); // Sử dụng biến final
                return image;
            }).collect(Collectors.toList());

            imageRepository.saveAll(images);
            savedBlog.setImages(images);
        }

        // Lưu thay đổi vào DB
        blog = blogRepository.save(blog);
        return blogMapper.toBlogResponse(blog);
    }


    @Override
    public BlogResponse updateStatus(String id, UpdateStatusBlogRequest request) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_FOUND));

        blog.setStatus(BlogStatus.valueOf(request.getStatus()));
        blog = blogRepository.save(blog);
        return blogMapper.toBlogResponse(blog);
    }

    @Override
    public void deleteBlog(String id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_FOUND));

        // Xóa ảnh trước
        imageRepository.deleteAll(blog.getImages());

        blogRepository.delete(blog);
    }

    @Override
    public BlogResponse updateBlogStatus(String id, UpdateStatusBlogRequest request) {
        return updateStatus(id, request);
    }
}
