package com.pacific.pacificbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PacificBeApplication {
    public static void main(String[] args) {
        SpringApplication.run(PacificBeApplication.class, args);
    }
}
