package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.request.CategoryRequest;
import com.pacific.pacificbe.dto.response.CategoryResponse;
import com.pacific.pacificbe.services.CategoryService;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UrlMapping.CATEGORY)
@RequiredArgsConstructor
@Tag(name = "Category Controller", description = "Quản lý danh mục (Category) của các tour du lịch")
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "Tạo mới danh mục", description = "Tạo mới một danh mục và trả về thông tin của danh mục vừa tạo.")
    @PostMapping(UrlMapping.CREATE_CATEGORY)
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lấy danh mục theo ID", description = "Trả về thông tin của danh mục dựa trên ID")
    @GetMapping(UrlMapping.GET_CATEGORY_BY_ID)
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable String id) {
        CategoryResponse response = categoryService.getCategoryById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lấy danh sách danh mục", description = "Trả về danh sách tất cả các danh mục")
    @GetMapping(UrlMapping.GET_ALL_CATEGORY)
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> response = categoryService.getAllCategories();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cập nhật danh mục", description = "Cập nhật thông tin của danh mục dựa trên ID")
    @PutMapping(UrlMapping.UPDATE_CATEGORY)
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable String id, @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Xóa danh mục", description = "Xóa danh mục dựa trên ID")
    @DeleteMapping(UrlMapping.DELETE_CATEGORY)
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Tìm kiếm danh mục", description = "Tìm kiếm danh mục dựa trên tiêu đề (title) hoặc trạng thái (status)")
    @GetMapping(UrlMapping.SEARCH_CATEGORY)
    public ResponseEntity<List<CategoryResponse>> searchCategories(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status) {
        List<CategoryResponse> responses = categoryService.searchCategories(title, status);
        return ResponseEntity.ok(responses);
    }
}
