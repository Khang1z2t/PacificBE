package com.pacific.pacificbe.utils;

import com.pacific.pacificbe.model.BlogCategory;
import com.pacific.pacificbe.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.Normalizer;

@Component
@RequiredArgsConstructor
public class SlugUtils {
    private final BlogRepository blogRepository;

    public String generateSlug(String title) {
        // data test
        // title = "Chữ tiếng Việt có đầy đủ ă â đ ê ô ơ ư và các dấu sắc, huyền, hỏi, ngã, nặng: á à ả ã ạ, ấ ầ ẩ ẫ ậ, é è ẻ ẽ ẹ, í ì ỉ ĩ ị, ó ò ỏ õ ọ, ú ù ủ ũ ụ, ý ỳ ỷ ỹ ỵ.";
        if (title == null || title.trim().isEmpty()) {
            return "";
        }
        String noAccents = Normalizer.normalize(title, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("đ", "d")
                .replaceAll("Đ", "d");

        String slug = noAccents.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "") // Loại bỏ ký tự đặc biệt
                .replaceAll("\\s+", "-")         // Thay khoảng trắng bằng dấu gạch nối
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "") // Loại bỏ nhiều dấu gạch nối liên tiếp
                .trim();
        return ensureUniqueSlug(slug);
    }

    public String generateSlug(String title, BlogCategory category) {
        String slug = generateSlug(title);
        String finalSlug = slug;
        if (category != null && category.getSlug() != null && !category.getSlug().isEmpty()) {
            finalSlug = category.getSlug() + "/" + slug;
        }
        finalSlug = ensureUniqueSlug(finalSlug);
        return finalSlug;
    }

    private String ensureUniqueSlug(String slug) {
        String uniqueSlug = slug;
        int counter = 1;

        while (blogRepository.existsBySlug(uniqueSlug)) {
            String baseSlug = slug.substring(0, slug.lastIndexOf("/") + 1);
            String titleSlug = slug.substring(slug.lastIndexOf("/") + 1);
            uniqueSlug = baseSlug + titleSlug + "-" + counter;
            counter++;
        }

        return uniqueSlug;
    }
}
