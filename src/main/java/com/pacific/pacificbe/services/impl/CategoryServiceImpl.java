package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.CategoryRequest;
import com.pacific.pacificbe.dto.response.CategoryResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.CategoryMapper;
import com.pacific.pacificbe.model.Category;
import com.pacific.pacificbe.repository.CategoryRepository;
import com.pacific.pacificbe.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @CacheEvict(value = "allCategories", allEntries = true) // Xóa cache danh sách khi thêm mới
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = categoryMapper.toEntity(request);
        category = categoryRepository.save(category);
        return categoryMapper.toResponse(category);
    }

    @Override
    @Cacheable(value = "categoryById", key = "#id") // Cache theo id
    public CategoryResponse getCategoryById(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return categoryMapper.toResponse(category);
    }

    @Override
    @Cacheable(value = "allCategories") // Cache toàn bộ danh sách
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = {"allCategories", "categoryById"}, key = "#id") // Xóa cache liên quan khi cập nhật
    public CategoryResponse updateCategory(String id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setStatus(request.getStatus());
        category.setTitle(request.getTitle());
        category.setType(request.getType());
        category = categoryRepository.save(category);
        return categoryMapper.toResponse(category);
    }

    @Override
    @Cacheable(value = "searchCategories", key = "#title + '-' + #status") // Cache theo title và status
    public List<CategoryResponse> searchCategories(String title, String status) {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .filter(cat -> {
                    if (title != null && !title.isBlank()) {
                        return cat.getTitle() != null && cat.getTitle().contains(title);
                    }
                    return true;
                })
                .filter(cat -> {
                    if (status != null && !status.isBlank()) {
                        return cat.getStatus() != null && cat.getStatus().equalsIgnoreCase(status);
                    }
                    return true;
                })
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = {"allCategories", "categoryById"}, key = "#id") // Xóa cache liên quan khi xóa
    public void deleteCategory(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        if (!category.getTours().isEmpty()) {
            throw new AppException(ErrorCode.CATEGORY_IN_USE);
        }
        categoryRepository.delete(category);
    }
}