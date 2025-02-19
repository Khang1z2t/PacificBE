package com.pacific.pacificbe.utils;

import com.pacific.pacificbe.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class AuthenUtils {
    public String generateOtp() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            User userDetails = (User) authentication.getPrincipal();
            return userDetails.getId(); // Giả định rằng bạn đã tùy chỉnh UserDetails để chứa ID của người dùng
        }
        return null;
    }
}
