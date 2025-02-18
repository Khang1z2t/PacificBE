package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.request.LoginRequest;
import com.pacific.pacificbe.dto.request.UserRegisterRequest;
import com.pacific.pacificbe.dto.response.AuthenticationResponse;
import com.pacific.pacificbe.dto.response.UserRegisterResponse;
import com.pacific.pacificbe.dto.response.UserResponse;
import com.pacific.pacificbe.services.UserService;
import com.pacific.pacificbe.utils.JavaMail;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    private final JavaMail javaMail;

    @PostMapping(UrlMapping.LOGIN)
    @Operation(summary = "API đăng nhập")
    ResponseEntity<ApiResponse<AuthenticationResponse>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Thành công", userService.loginUser(request)));
    }

    @PostMapping(UrlMapping.REGISTER)
    @Operation(summary = "API đăng kí tài khoản mới")
    ResponseEntity<ApiResponse<UserRegisterResponse>> register(@RequestBody UserRegisterRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Thành công", userService.registerUser(request)));
    }

    @GetMapping(UrlMapping.AUTHENTICATE_TOKEN)
    @Operation(summary = "API xác thực token")
    ResponseEntity<ApiResponse<UserResponse>> authenticateToken() {
        return ResponseEntity.ok(new ApiResponse<>(200, "Thành công", userService.authenticateToken()));
    }

    @GetMapping("test-mail")
    @Operation(summary = "API test gửi mail")
    ResponseEntity<?> testMail() {
        javaMail.sendEmail("kbao040@gmail.com", "Test", "Test");
        return ResponseEntity.ok("OK");
    }
}
