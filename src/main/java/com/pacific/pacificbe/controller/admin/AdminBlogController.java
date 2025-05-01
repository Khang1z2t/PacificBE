package com.pacific.pacificbe.controller.admin;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.request.*;
import com.pacific.pacificbe.dto.request.blog.BlogCategoryRequest;
import com.pacific.pacificbe.dto.response.blog.BlogCategoryResponse;
import com.pacific.pacificbe.dto.response.blog.BlogResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.services.BlogService;
import com.pacific.pacificbe.services.GoogleDriveService;
import com.pacific.pacificbe.utils.UrlMapping;
import com.pacific.pacificbe.utils.enums.FolderType;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(UrlMapping.ADMIN_BLOG)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminBlogController {

    BlogService blogService;
    GoogleDriveService googleDriveService;

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

    @PostMapping(value = UrlMapping.CREATE_BLOG, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Tạo mới một bài Blog")
    public ResponseEntity<ApiResponse<BlogResponse>> createBlog(@Valid @ModelAttribute BlogRequest request,
                                                                @RequestParam(required = false) MultipartFile thumbnail) {
        BlogResponse newBlog = blogService.createBlog(request, thumbnail);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Tạo bài blog thành công", newBlog));
    }

    @PutMapping(value = UrlMapping.UPDATE_BLOG, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Cập nhật thông tin bài blog")
    public ResponseEntity<ApiResponse<BlogResponse>> updateBlog(
            @PathVariable String id,
            @Valid @ModelAttribute BlogRequest request,
            @RequestParam(required = false) MultipartFile thumbnail) {
        BlogResponse updatedBlog = blogService.updateBlog(id, request, thumbnail);
        return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật thông tin thành công", updatedBlog));
    }

    @GetMapping(value = UrlMapping.GET_BLOG_BY_SLUG)
    @Operation(summary = "Lấy thông tin bài blog theo slug")
    public ResponseEntity<ApiResponse<BlogResponse>> getBlogBySlug(@RequestParam String slug,
                                                                   HttpServletRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(200,
                "Lấy thông tin thành công", blogService.getBlogBySlug(slug, request)));
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
            String imageUrl = googleDriveService.uploadImageToDrive(file, FolderType.RESOURCES);
            imageUrls.add(imageUrl);
        }

        return ResponseEntity.ok(new ApiResponse<>(200, "Upload ảnh thành công", imageUrls));
    }

    @GetMapping(UrlMapping.GET_BLOG_CATEGORY)
    @Operation(summary = "Lấy danh sách tất cả danh mục blog")
    public ResponseEntity<ApiResponse<List<BlogCategoryResponse>>> getAllBlogCategories() {
        List<BlogCategoryResponse> categories = blogService.getAllBlogCategories();
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy danh mục thành công", categories));
    }

    @PostMapping(UrlMapping.CREATE_BLOG_CATEGORY)
    @Operation(summary = "Tạo mới một danh mục blog")
    public ResponseEntity<ApiResponse<BlogCategoryResponse>> createBlogCategory(@Valid @RequestBody BlogCategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Api đang trong quá trình thực hiện",
                        blogService.createBlogCategory(request)));
    }

    @PostMapping(UrlMapping.UPDATE_BLOG_CATEGORY)
    @Operation(summary = "Cập nhật thông tin danh mục blog")
    public ResponseEntity<ApiResponse<BlogCategoryResponse>> updateBlogCategory(
            @PathVariable String id,
            @Valid @RequestBody BlogCategoryRequest request) {
        BlogCategoryResponse updatedCategory = blogService.updateBlogCategory(id, request);
        return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật danh mục thành công", updatedCategory));
    }

    @DeleteMapping(UrlMapping.DELETE_BLOG_CATEGORY)
    @Operation(summary = "Xóa danh mục blog theo ID")
    public ResponseEntity<ApiResponse<Void>> deleteBlogCategory(@PathVariable String id) {
        blogService.deleteBlogCategory(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Xóa danh mục blog thành công", null));
    }

    @PostMapping(UrlMapping.SUBSCRIBE_BLOG)
    @Operation(summary = "Đăng ký nhận thông báo khi có bài viết mới")
    public ResponseEntity<ApiResponse<Boolean>> subscribeBlog(@RequestParam String email,
                                                              @RequestParam(required = false) String name) {
        return ResponseEntity.ok(new ApiResponse<>(200,
                "Đăng ký nhận thông báo thành công", blogService.subscribeBlog(email, name)));
    }

    @PostMapping(UrlMapping.UNSUBSCRIBE_BLOG)
    @Operation(summary = "Hủy đăng ký nhận thông báo khi có bài viết mới (token đã được mã hóa)")
    public ResponseEntity<ApiResponse<Boolean>> unsubscribeBlog(@PathVariable String token) {
        return ResponseEntity.ok(new ApiResponse<>(200,
                "Hủy đăng ký nhận thông báo thành công", blogService.unsubscribeBlog(token)));
    }

}
