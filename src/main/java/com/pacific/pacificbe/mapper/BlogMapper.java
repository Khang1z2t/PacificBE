package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.response.blog.BlogCategoryResponse;
import com.pacific.pacificbe.dto.response.blog.BlogCategorySimpleResponse;
import com.pacific.pacificbe.dto.response.blog.BlogResponse;
import com.pacific.pacificbe.dto.response.tour.TourSimpleResponse;
import com.pacific.pacificbe.dto.response.user.UserSimpleResponse;
import com.pacific.pacificbe.model.Blog;
import com.pacific.pacificbe.model.BlogCategory;
import com.pacific.pacificbe.utils.IdUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlogMapper {
    final ModelMapper modelMapper;
    private final IdUtil idUtil;

    public BlogResponse toBlogResponse(Blog blog) {
        BlogResponse response = modelMapper.map(blog, BlogResponse.class);
        response.setThumbnail(blog.getThumbnailUrl() != null ? idUtil.getIdImage(blog.getThumbnailUrl()) : null);
        response.setUser(modelMapper.map(blog.getUser(), UserSimpleResponse.class));
        response.setCategory(modelMapper.map(blog.getCategory(), BlogCategorySimpleResponse.class));
        response.setTours(blog.getTours().stream()
                .map(tour -> modelMapper.map(tour, TourSimpleResponse.class))
                .collect(Collectors.toList()));
        return response;
    }

    public Blog toBlog(BlogResponse blogResponse) {
        return modelMapper.map(blogResponse, Blog.class);
    }

    public List<BlogResponse> toBlogResponseList(List<Blog> blogs) {
        return blogs.stream()
                .map(this::toBlogResponse)
                .collect(Collectors.toList());
    }

    public BlogCategoryResponse toBlogCategoryResponse(BlogCategory blogCategory) {
        return modelMapper.map(blogCategory, BlogCategoryResponse.class);
    }

    public List<BlogCategoryResponse> toBlogCategoryResponseList(List<BlogCategory> blogCategories) {
        return blogCategories.stream()
                .map(this::toBlogCategoryResponse)
                .collect(Collectors.toList());
    }
}
