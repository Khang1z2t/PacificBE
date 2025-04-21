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
        if (title == null || title.trim().isEmpty()) {
            return "";
        }
        String noAccents = Normalizer.normalize(title, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        String slug = noAccents.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "") // Loại bỏ ký tự đặc biệt
                .replaceAll("\\s+", "-")         // Thay khoảng trắng bằng dấu gạch nối
                .replaceAll("-+", "-")           // Loại bỏ nhiều dấu gạch nối liên tiếp
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
