package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.response.BlogResponse;
import com.pacific.pacificbe.model.Blog;
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

    public BlogResponse toBlogResponse(Blog blog) {
        return modelMapper.map(blog, BlogResponse.class);
    }

    public Blog toBlog(BlogResponse blogResponse) {
        return modelMapper.map(blogResponse, Blog.class);
    }

    public List<BlogResponse> toBlogResponseList(List<Blog> blogs) {
        return blogs.stream()
                .map(this::toBlogResponse)
                .collect(Collectors.toList());
    }
}
