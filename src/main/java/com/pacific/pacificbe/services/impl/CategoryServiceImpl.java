package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.CategoryRequest;
import com.pacific.pacificbe.dto.response.CategoryResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.CategoryMapper;
import com.pacific.pacificbe.model.Category;
import com.pacific.pacificbe.repository.CategoryRepository;
import com.pacific.pacificbe.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = categoryMapper.toEntity(request);
        // Không cần set id thủ công nếu sử dụng @GeneratedValue(strategy = GenerationType.UUID)
        // category.setId(UUID.randomUUID().toString());
        category = categoryRepository.save(category);
        return categoryMapper.toResponse(category);
    }


    @Override
    public CategoryResponse getCategoryById(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return categoryMapper.toResponse(category);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
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
    public List<CategoryResponse> searchCategories(String title, String status) {
        // 1. Load all categories
        List<Category> categories = categoryRepository.findAll();

        // 2. Filter in memory
        return categories.stream()
                .filter(cat -> {
                    if (title != null && !title.isBlank()) {
                        // Only keep if cat.getTitle() contains "title"
                        return cat.getTitle() != null && cat.getTitle().contains(title);
                    }
                    // if title is null or blank, no filter needed
                    return true;
                })
                .filter(cat -> {
                    if (status != null && !status.isBlank()) {
                        // Only keep if cat.getStatus() equalsIgnoreCase "status"
                        return cat.getStatus() != null
                                && cat.getStatus().equalsIgnoreCase(status);
                    }
                    // if status is null or blank, no filter needed
                    return true;
                })
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
    }
    @Override
    public void deleteCategory(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        if (!category.getTours().isEmpty()) {
            throw new AppException(ErrorCode.CATEGORY_IN_USE);
        }
        categoryRepository.delete(category);
    }





}
