package com.pacific.pacificbe.controller.admin;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.request.*;
import com.pacific.pacificbe.dto.response.BlogResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.services.BlogService;
import com.pacific.pacificbe.services.FileStorageService;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(UrlMapping.ADMIN_BLOG)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminBlogController {

    BlogService blogService;
    FileStorageService fileStorageService;

    @GetMapping(UrlMapping.GET_ALL_BLOGS)
    @Operation(summary = "Lấy danh sách tất cả bài blog")
    public ResponseEntity<ApiResponse<List<BlogResponse>>> getAllBlogs() {
        List<BlogResponse> blogs = blogService.getAllBlogs();
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy danh sách thành công", blogs));
    }

    @GetMapping(UrlMapping.GET_BLOG_BY_TITLE)
    @Operation(summary = "Lấy thông tin bài blog theo tiêu đề")
    public ResponseEntity<ApiResponse<BlogResponse>> getBlogByTitle(@RequestParam String title) {
        BlogResponse blog = blogService.getBlogByTitle(title);
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy thông tin thành công", blog));
    }

    @PostMapping(UrlMapping.CREATE_BLOG)
    @Operation(summary = "Tạo mới một bài Blog")
    public ResponseEntity<ApiResponse<BlogResponse>> createBlog(@Valid @RequestBody BlogRequest request) {
        BlogResponse newBlog = blogService.createBlog(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Tạo bài blog thành công", newBlog));
    }

    @PutMapping(UrlMapping.UPDATE_BLOG)
    @Operation(summary = "Cập nhật thông tin bài blog")
    public ResponseEntity<ApiResponse<BlogResponse>> updateBlog(
            @PathVariable String id,
            @Valid @RequestBody UpdateBlogRequest request) {
        BlogResponse updatedBlog = blogService.updateBlog(id, request);
        return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật thông tin thành công", updatedBlog));
    }

    @PatchMapping(UrlMapping.UPDATE_STATUS_BLOG)
    @Operation(summary = "Cập nhật trạng thái bài blog")
    public ResponseEntity<ApiResponse<BlogResponse>> updateStatusBlog(
            @PathVariable String id,
            @Valid @RequestBody UpdateStatusBlogRequest request) {
        BlogResponse updatedBlog = blogService.updateStatus(id, request);
        return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật trạng thái thành công", updatedBlog));
    }

    @DeleteMapping(UrlMapping.DELETE_BLOG)
    @Operation(summary = "Xóa bài blog theo ID")
    public ResponseEntity<ApiResponse<Void>> deleteBlog(@PathVariable String id) {
        blogService.deleteBlog(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Xóa bài blog thành công", null));
    }

    @PostMapping(UrlMapping.UPLOAD_IMAGE)
    @Operation(summary = "Upload ảnh lên server")
    public ResponseEntity<ApiResponse<List<String>>> uploadImages(@RequestParam("files") List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new AppException(ErrorCode.FILE_NOT_FOUND);
        }

        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            System.out.println("File nhận được: " + file.getOriginalFilename()); // Debug
            String imageUrl = fileStorageService.storeFile(file);
            imageUrls.add(imageUrl);
        }

        return ResponseEntity.ok(new ApiResponse<>(200, "Upload ảnh thành công", imageUrls));
    }

}
