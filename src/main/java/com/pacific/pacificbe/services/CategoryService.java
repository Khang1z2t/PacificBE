package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.CategoryRequest;
import com.pacific.pacificbe.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse getCategoryById(String id);
    List<CategoryResponse> getAllCategories();
    CategoryResponse updateCategory(String id, CategoryRequest request);
    List<CategoryResponse> searchCategories(String title, String status);
    void deleteCategory(String id);

}
