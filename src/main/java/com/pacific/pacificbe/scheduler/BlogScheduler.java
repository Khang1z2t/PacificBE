package com.pacific.pacificbe.scheduler;

import com.pacific.pacificbe.model.Blog;
import com.pacific.pacificbe.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.pacific.pacificbe.utils.Constant.BLOG_VIEW_KEYS;

@Slf4j
@Component
@RequiredArgsConstructor
public class BlogScheduler {
    private final RedisTemplate<String, String> redisTemplate;
    private final BlogRepository blogRepository;

    @Scheduled(fixedRate = 300000) // Mỗi 5 phút
    @Transactional
    public void updateBlogView() {
        Set<String> viewKeys = redisTemplate.keys(BLOG_VIEW_KEYS + "*");
        if (viewKeys.isEmpty()) {
            return;
        }
        for (String key : viewKeys) {
            String slug = key.replace(BLOG_VIEW_KEYS, "");
            String viewCountStr = redisTemplate.opsForValue().get(key);
            if (viewCountStr != null) {
                int views = Integer.parseInt(viewCountStr);
                if (views > 0) {
                    // Cập nhật viewCount trong cơ sở dữ liệu
                    Blog blog = blogRepository.findBySlug(slug).orElse(null);
                    if (blog != null) {
                        blog.setViewCount(blog.getViewCount() + views);
                        blogRepository.save(blog);
                    }
                    // Xóa key sau khi cập nhật
                    redisTemplate.delete(key);
                }
            }
        }
    }
}
