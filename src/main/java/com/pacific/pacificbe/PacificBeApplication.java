package com.pacific.pacificbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching // cache
@EnableScheduling // tự chạy sau 1 khoảng tg, tác vụ định kỳ
@EnableJpaAuditing // tự động trong jpa khi có hành động nào đó
@EnableFeignClients // all api thứ 3 hoặc api của mình
@SpringBootApplication
public class PacificBeApplication {
    public static void main(String[] args) {
        SpringApplication.run(PacificBeApplication.class, args);
    }
}
