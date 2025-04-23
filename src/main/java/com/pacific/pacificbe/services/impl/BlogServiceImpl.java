package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.BlogRequest;
import com.pacific.pacificbe.dto.request.UpdateBlogRequest;
import com.pacific.pacificbe.dto.request.UpdateStatusBlogRequest;
import com.pacific.pacificbe.dto.response.blog.BlogCategoryResponse;
import com.pacific.pacificbe.dto.response.blog.BlogResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.BlogMapper;
import com.pacific.pacificbe.model.*;
import com.pacific.pacificbe.repository.*;
import com.pacific.pacificbe.services.BlogService;
import com.pacific.pacificbe.services.GoogleDriveService;
import com.pacific.pacificbe.utils.AuthUtils;
import com.pacific.pacificbe.utils.HtmlSanitizerUtil;
import com.pacific.pacificbe.utils.IdUtil;
import com.pacific.pacificbe.utils.SlugUtils;
import com.pacific.pacificbe.utils.enums.BlogStatus;
import com.pacific.pacificbe.utils.enums.FolderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import static com.pacific.pacificbe.utils.Constant.MAX_META_DESCRIPTION_LENGTH;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final ImageRepository imageRepository;
    private final BlogMapper blogMapper;
    private final HtmlSanitizerUtil htmlSanitizerUtil;
    private final UserRepository userRepository;
    private final SlugUtils slugUtils;
    private final BlogCategoryRepository blogCategoryRepository;
    private final TourRepository tourRepository;
    private final IdUtil idUtil;
    private final GoogleDriveService googleDriveService;

    @Override
    @Transactional
    public BlogResponse createBlog(BlogRequest request, MultipartFile thumbnail) {
        String userId = AuthUtils.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        String updatedContent = processHtmlContent(request.getContent());
        Blog blog = new Blog();
        blog.setTitle(request.getTitle());
        blog.setContent(updatedContent);
        blog.setStatus(request.getStatus());
        blog.setMetaTitle(request.getTitle());
        blog.setMetaDescription(generateMetaDescription(updatedContent));
        blog.setUser(user);
        blog.setViewCount(0);
        blog.setLikeCount(0);
        if (thumbnail != null && !thumbnail.isEmpty()) {
            String thumbnailUrl = googleDriveService.uploadImageToDrive(thumbnail, FolderType.RESOURCES);
            blog.setThumbnailUrl(thumbnailUrl);
        }
        BlogCategory category = null;
        if (request.getCategoryId() != null) {
            category = blogCategoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
            blog.setCategory(category);
        }
        blog.setSlug(slugUtils.generateSlug(request.getTitle(), category));
        if (request.getTourId() != null && !request.getTourId().isEmpty()) {
            List<Tour> tours = tourRepository.findAllById(request.getTourId());
            blog.setTours(tours);
        }
        blogRepository.save(blog);
        return blogMapper.toBlogResponse(blog);
    }


    @Override
    public BlogResponse getBlogByTitle(String title) {
        Blog blog = blogRepository.findByTitle(title)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_FOUND));
        return blogMapper.toBlogResponse(blog);
    }

    @Override
    public List<BlogResponse> getAllBlogs() {
        List<Blog> blogs = blogRepository.findAll();
        return blogMapper.toBlogResponseList(blogs);
    }

    @Override
    public BlogResponse updateBlog(String id, UpdateBlogRequest request) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_FOUND));

        // Cập nhật thông tin cơ bản
        blog.setTitle(request.getTitle());
        blog.setContent(request.getContent());
        blog.setStatus(BlogStatus.valueOf(request.getStatus()));

        // Xóa ảnh cũ trong DB

        final Blog savedBlog = blog;

        // Cập nhật danh sách ảnh mới
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            List<Image> images = request.getImageUrls().stream().map(url -> {
                Image image = new Image();
                image.setImageUrl(url);
                return image;
            }).collect(Collectors.toList());

            imageRepository.saveAll(images);
        }

        // Lưu thay đổi vào DB
        blog = blogRepository.save(blog);
        return blogMapper.toBlogResponse(blog);
    }


    @Override
    public BlogResponse updateStatus(String id, UpdateStatusBlogRequest request) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_FOUND));

        blog.setStatus(BlogStatus.valueOf(request.getStatus()));
        blog = blogRepository.save(blog);
        return blogMapper.toBlogResponse(blog);
    }

    @Override
    public void deleteBlog(String id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_FOUND));

        // Xóa ảnh trước

        blogRepository.delete(blog);
    }

    @Override
    public BlogResponse updateBlogStatus(String id, UpdateStatusBlogRequest request) {
        return updateStatus(id, request);
    }

    @Override
    public BlogResponse getBlogById(String id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_FOUND));
        return blogMapper.toBlogResponse(blog);
    }

    @Override
    public BlogResponse getBlogBySlug(String slug) {
        Blog blog = blogRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_FOUND));
        return blogMapper.toBlogResponse(blog);
    }

    @Override
    public List<BlogCategoryResponse> getAllBlogCategories() {
        var blogCategory = blogCategoryRepository.findAll();
        return blogMapper.toBlogCategoryResponseList(blogCategory);
    }

    private String processHtmlContent(String html) {
        String safeHtml = htmlSanitizerUtil.sanitize(html);
        Document doc = Jsoup.parse(safeHtml);
        Elements imgTags = doc.select("img[src^=data:image/]");
        for (Element img : imgTags) {
            String src = img.attr("src");
            if (src.startsWith("data:image/")) {
                try {
                    // Trích xuất phần Base64 (bỏ header như "data:image/jpeg;base64,")
                    String base64String = src.split(",")[1];

                    byte[] imageBytes = Base64.getDecoder().decode(base64String);

                    MultipartFile multipartFile = new MockMultipartFile(
                            "file",
                            "image-blog.jpg",
                            "image/jpeg",
                            imageBytes
                    );

                    String imageId = idUtil.getIdImage(
                            googleDriveService.uploadImageToDrive(multipartFile, FolderType.RESOURCES));

                    String srcAttr = "${config.imageConfig.getImage('" + imageId + "')}";
                    img.attr("src", srcAttr);
                } catch (Exception e) {
                    log.error("Error while processing image in blog: {}", e.getMessage());
                    img.attr("src", "/images/placeholder.jpg");
                }
            }
        }
        return doc.body().html();
    }

    private String generateMetaDescription(String htmlContent) {
        if (htmlContent == null || htmlContent.trim().isEmpty()) {
            return "";
        }
        // Loại bỏ HTML và lấy văn bản thuần túy
        String plainText = Jsoup.parse(htmlContent).text();

        // Loại bỏ khoảng trắng dư thừa và chuẩn hóa
        plainText = plainText.replaceAll("\\s+", " ").trim();

        // Cắt tối đa 160 ký tự
        if (plainText.length() <= MAX_META_DESCRIPTION_LENGTH) {
            return plainText;
        }

        // Cắt và thêm dấu ba chấm
        String truncated = plainText.substring(0, MAX_META_DESCRIPTION_LENGTH).trim();
        // Đảm bảo không cắt giữa từ
        int lastSpace = truncated.lastIndexOf(" ");
        if (lastSpace > 0) {
            truncated = truncated.substring(0, lastSpace);
        }
        return truncated + "...";
    }
}
