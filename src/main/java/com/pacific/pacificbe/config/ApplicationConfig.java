package com.pacific.pacificbe.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pacific.pacificbe.repository.TourRepository;
import com.pacific.pacificbe.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.TimeZone;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class ApplicationConfig {
    UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return identifier -> userRepository.findByUsernameOrEmail(identifier, identifier)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    @Bean
    CommandLineRunner runner(
            @Autowired(required = false) BuildProperties buildProperties,
            @Autowired(required = false) GitProperties gitProperties
    ) {
        return args -> {
            log.info("---------------------------------------------");

            // 1. Lấy Base Version từ pom.xml (nếu không có thì default)
            String version = (buildProperties != null) ? buildProperties.getVersion() : "0.0.1";

            // 2. Xử lý hiển thị
            if (gitProperties != null) {
                // --- TRƯỜNG HỢP: PRODUCTION / DOCKER / RENDER ---
                // Đã build qua maven nên có git info
                String commitId = gitProperties.getShortCommitId();
                String branch = gitProperties.getBranch();

                log.info("🚀 App Mode: PRODUCTION / BUILD");
                log.info("📦 Version:  {} (Commit: {})", version, commitId);
                log.info("🌿 Branch:   {}", branch);
                log.info("Build Time: {}", (buildProperties != null ? buildProperties.getTime() : "N/A"));
            } else {
                // --- TRƯỜNG HỢP: LOCAL (IDE) ---
                // Chưa chạy maven build nên chưa có file git.properties
                log.info("💻 App Mode: LOCAL DEVELOPMENT");
                log.info("🔧 Version:  {}.dev", version);
            }

            log.info("---------------------------------------------");
        };
    }

   @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
    }
}
