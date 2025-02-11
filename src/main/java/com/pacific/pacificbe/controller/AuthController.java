//package com.pacific.pacificbe.controller;
//
//import com.pacific.pacificbe.dto.ApiResponse;
//import com.pacific.pacificbe.dto.request.LoginRequest;
//import com.pacific.pacificbe.dto.response.AuthenticationResponse;
//import com.pacific.pacificbe.dto.response.UserResponse;
//import com.pacific.pacificbe.services.AuthService;
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class AuthController {
//    AuthService authService;
//
//    @PostMapping("/login")
//    @Operation(summary = "API đăng nhập")
//    ResponseEntity<ApiResponse<AuthenticationResponse>> login(@RequestBody LoginRequest request) {
//        return ResponseEntity.ok(ApiResponse.<AuthenticationResponse>builder()
//                .data(authService.loginUser(request))
//                .build());
//    }
//
//    @PostMapping("/register")
//    @Operation(summary = "API đăng kí tài khoản mới")
//    void register() {
//        Object o = null;
//    }
//}
