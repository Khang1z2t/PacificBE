package com.pacific.pacificbe;

import com.pacific.pacificbe.repository.TourRepository;
import com.pacific.pacificbe.utils.enums.TourStatus;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching // cache
@EnableScheduling // tự chạy sau 1 khoảng tg, tác vụ định kỳ
@EnableJpaAuditing // tự động trong jpa khi có hành động nào đó
@EnableFeignClients // all api thứ 3 hoặc api của mình
@EnableAspectJAutoProxy // tự động tạo proxy cho các class có @Aspect
@SpringBootApplication
public class PacificBeApplication {
    public static void main(String[] args) {
        SpringApplication.run(PacificBeApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(TourRepository tourRepository) {
        return args -> {
            // Code khởi tạo dữ liệu hoặc thực hiện các tác vụ khác khi ứng dụng khởi động

//            System.out.println(tourRepository.findAllWithFilters(null, null, null, null, null, null));
        };
    }
}
