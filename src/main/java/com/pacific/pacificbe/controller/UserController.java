package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.request.LoginRequest;
import com.pacific.pacificbe.dto.request.ResetUserPasswordRequest;
import com.pacific.pacificbe.dto.request.UserRegisterRequest;
import com.pacific.pacificbe.dto.request.VerifyOtpRequest;
import com.pacific.pacificbe.dto.response.AuthenticationResponse;
import com.pacific.pacificbe.dto.response.UserRegisterResponse;
import com.pacific.pacificbe.dto.response.UserResponse;
import com.pacific.pacificbe.model.User;
import com.pacific.pacificbe.repository.UserRepository;
import com.pacific.pacificbe.services.UserService;
import com.pacific.pacificbe.utils.JavaMail;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlMapping.USERS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    private final JavaMail javaMail;
    private final UserRepository userRepository;


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(UrlMapping.GET_ALL_USERS)
    @Operation(summary = "API lấy danh sách tất cả người dùng")
    ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(new ApiResponse<>(200, "Thành công", userService.getAllUsers()));
    }

    @GetMapping("test-mail")
    @Operation(summary = "API test gửi mail")
    ResponseEntity<?> testMail(@RequestParam String email, @RequestParam String subject, @RequestParam String content) {
        javaMail.sendEmail(email, subject, content);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/test-user")
    @Operation(summary = "API test tạo user")
    ResponseEntity<?> testaddUser() {
        userRepository.save(User.builder().password("12345678").build());
        return ResponseEntity.ok("OK");
    }
}
