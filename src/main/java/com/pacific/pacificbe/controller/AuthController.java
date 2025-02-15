package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.ApiResponse.ApiResponseBuilder;
import com.pacific.pacificbe.dto.request.LoginRequest;
import com.pacific.pacificbe.dto.request.UserRegisterRequest;
import com.pacific.pacificbe.dto.response.AuthenticationResponse;
import com.pacific.pacificbe.dto.response.UserRegisterResponse;
import com.pacific.pacificbe.dto.response.UserResponse;
import com.pacific.pacificbe.services.AuthService;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UrlMapping.AUTH)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;

    @PostMapping(UrlMapping.LOGIN)
    @Operation(summary = "API đăng nhập")
    ResponseEntity<ApiResponse<AuthenticationResponse>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok((ApiResponse.<AuthenticationResponse>builder()
                .data(authService.loginUser(request)))
                .build());
    }

    @PostMapping(UrlMapping.REGISTER)
    @Operation(summary = "API đăng kí tài khoản mới")
    ResponseEntity<ApiResponse<UserRegisterResponse>> register(@RequestBody UserRegisterRequest request) {
        return ResponseEntity.ok((ApiResponse.<UserRegisterResponse>builder()
                .data(authService.registerUser(request)))
                .build());
    }
}
