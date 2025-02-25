package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.request.CategoryRequest;
import com.pacific.pacificbe.dto.response.CategoryResponse;
import com.pacific.pacificbe.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category Controller", description = "Category operations")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Tạo mới Category", description = "Tạo mới 1 category và trả về thông tin vừa tạo.")
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lấy Category theo ID", description = "Trả về thông tin category (bao gồm danh sách tour IDs) dựa trên ID.")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable String id) {
        CategoryResponse response = categoryService.getCategoryById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lấy danh sách Category", description = "Trả về danh sách tất cả các category.")
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> response = categoryService.getAllCategories();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cập nhật Category", description = "Cập nhật thông tin category dựa trên ID.")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable String id, @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(response);
    }

    // 1. Search endpoint
    @Operation(summary = "Tìm kiếm danh mục", description = "Tìm kiếm category theo title hoặc status.")
    @GetMapping("/search")
    public ResponseEntity<List<CategoryResponse>> searchCategories(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status
    ) {
        List<CategoryResponse> responses = categoryService.searchCategories(title, status);
        return ResponseEntity.ok(responses);
    }

    // 2. Delete endpoint
    @Operation(summary = "Xóa Category", description = "Xóa 1 category dựa trên ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}
