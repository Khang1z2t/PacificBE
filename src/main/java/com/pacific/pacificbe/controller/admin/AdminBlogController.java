package com.pacific.pacificbe.controller.admin;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.request.*;
import com.pacific.pacificbe.dto.response.BlogResponse;
import com.pacific.pacificbe.services.BlogService;
import com.pacific.pacificbe.services.FileStorageService;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
    ResponseEntity<ApiResponse<List<BlogResponse>>> getAllBlogs() {
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy danh sách thành công", blogService.getAllBlogs()));
    }


    @GetMapping(UrlMapping.GET_BLOG_BY_TITLE)
    @Operation(summary = "Lấy thông tin bài blog theo tiêu đề")
    public ResponseEntity<ApiResponse<BlogResponse>> getBlogByTitle(@RequestParam String title) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy thông tin thành công", blogService.getBlogByTitle(title)));
    }

    @PostMapping(UrlMapping.CREATE_BLOG)
    @Operation(summary = "Tạo mới một bài Blog")
    public ResponseEntity<ApiResponse<BlogResponse>> createBlog(@RequestBody BlogRequest request) {
        BlogResponse newBlog = blogService.createBlog(request);
        return ResponseEntity.ok(new ApiResponse<>(201, "Tạo bài blog thành công", newBlog));
    }


    @PutMapping(UrlMapping.UPDATE_BLOG)
    @Operation(summary = "Cập nhật thông tin bài blog")
    public ResponseEntity<ApiResponse<BlogResponse>> updateBlog(@PathVariable String id, @RequestBody UpdateBlogRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật thông tin thành công", blogService.updateBlog(id, request)));
    }

    @PatchMapping(UrlMapping.UPDATE_STATUS_BLOG)
    @Operation(summary = "Cập nhật trạng thái bài blog")
    public ResponseEntity<ApiResponse<BlogResponse>> updateStatusBlog(
            @PathVariable String id,
            @RequestBody UpdateStatusBlogRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật trạng thái thành công", blogService.updateStatus(id, request)));
    }

    @DeleteMapping(UrlMapping.DELETE_BLOG)
    @Operation(summary = "Xóa bài blog theo ID")
    public ResponseEntity<ApiResponse<Void>> deleteBlog(@PathVariable String id) {
        blogService.deleteBlog(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Xóa bài blog thành công", null));
    }

    @PostMapping("/upload-images")
    public ResponseEntity<ApiResponse<List<String>>> uploadImages(@RequestParam("files") List<MultipartFile> files) {
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            String imageUrl = fileStorageService.storeFile(file); // Hàm lưu file vào server
            imageUrls.add(imageUrl);
        }

        return ResponseEntity.ok(new ApiResponse<>(200, "Upload ảnh thành công", imageUrls));
    }

}
