package com.pacific.pacificbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
public class PacificBeApplication {
    public static void main(String[] args) {
        SpringApplication.run(PacificBeApplication.class, args);
    }
}
