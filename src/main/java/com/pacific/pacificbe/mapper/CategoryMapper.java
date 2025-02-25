package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.request.CategoryRequest;
import com.pacific.pacificbe.dto.response.CategoryResponse;
import com.pacific.pacificbe.model.Category;
import com.pacific.pacificbe.model.Tour;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequest request) {
        Category category = new Category();
        category.setStatus(request.getStatus());
        category.setTitle(request.getTitle());
        category.setType(request.getType());
        return category;
    }

    public CategoryResponse toResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setStatus(category.getStatus());
        response.setTitle(category.getTitle());
        response.setType(category.getType());
        if (category.getTours() != null) {
            List<String> tourIds = category.getTours().stream()
                    .map(Tour::getId)
                    .collect(Collectors.toList());
            response.setTourIds(tourIds);
        } else {
            response.setTourIds(new ArrayList<>());
        }
        return response;
    }
}
