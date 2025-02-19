package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.request.LoginRequest;
import com.pacific.pacificbe.dto.request.ResetUserPasswordRequest;
import com.pacific.pacificbe.dto.request.UserRegisterRequest;
import com.pacific.pacificbe.dto.request.VerifyOtpRequest;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping(UrlMapping.SEND_VERIFY_MAIL)
    @Operation(summary = "API gửi mail xác thực")
    ResponseEntity<?> sendVerifyMail(@RequestParam String email) {
        return ResponseEntity.ok(userService.sendEmailVerify(email));
    }

    @PostMapping(UrlMapping.VERIFY_EMAIL)
    @Operation(summary = "API xác thực email")
    ResponseEntity<?> verifyEmail(@RequestBody VerifyOtpRequest request) {
        return ResponseEntity.ok(userService.verifyEmail(request));
    }

    @PostMapping(UrlMapping.SEND_RESET_PASSWORD_MAIL)
    @Operation(summary = "API gửi mail reset mật khẩu")
    ResponseEntity<?> sendResetPasswordMail(@RequestParam String email) {
        return ResponseEntity.ok(userService.sendEmailResetPassword(email));
    }

    @PostMapping(UrlMapping.VERIFY_RESET_PASSWORD)
    @Operation(summary = "API xác thực reset mật khẩu")
    ResponseEntity<?> verifyResetPassword(@RequestBody VerifyOtpRequest request) {
        return ResponseEntity.ok(userService.verifyResetPassword(request));
    }

    @PostMapping(UrlMapping.RESET_PASSWORD)
    @Operation(summary = "API reset mật khẩu")
    ResponseEntity<?> resetPassword(@RequestBody ResetUserPasswordRequest request) {
        return ResponseEntity.ok(userService.resetPassword(request));
    }

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
}
