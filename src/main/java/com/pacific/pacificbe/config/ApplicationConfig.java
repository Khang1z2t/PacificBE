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
    CommandLineRunner runner(@Autowired(required = false) BuildProperties buildProperties) {
        return args -> {
            log.info("App đã khởi động!");

            if (buildProperties != null) {
                // Khi chạy trên Docker/Render (đã build)
                log.info("Version: {}", buildProperties.getVersion());
                log.info("Build Time: {}", buildProperties.getTime());
            } else {
                // Khi chạy Local (chưa build)
                log.info("Build Info: Đang chạy Local (Development Mode)");
            }

        };
    }

   @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
    }
}
