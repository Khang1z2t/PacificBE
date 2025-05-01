package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.request.*;
import com.pacific.pacificbe.dto.response.AuthenticationResponse;
import com.pacific.pacificbe.dto.response.UserRegisterResponse;
import com.pacific.pacificbe.dto.response.UserResponse;
import com.pacific.pacificbe.services.AuthService;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping(UrlMapping.AUTH)
@RequiredArgsConstructor
@Tag(name = "Auth  Controller", description = "Các api kiểm tra người dùng")
public class AuthController {
    private final AuthService authService;

    @PostMapping(UrlMapping.LOGIN)
    @Operation(summary = "API đăng nhập")
    ResponseEntity<ApiResponse<AuthenticationResponse>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Thành công", authService.loginUser(request)));
    }

    @PostMapping(UrlMapping.REGISTER)
    @Operation(summary = "API đăng kí tài khoản mới")
    ResponseEntity<ApiResponse<UserRegisterResponse>> register(@RequestBody UserRegisterRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Thành công", authService.registerUser(request)));
    }

    @GetMapping(UrlMapping.OAUTH2_GOOGLE)
    @Operation(summary = "API đăng nhập bằng google")
    ResponseEntity<ApiResponse<String>> loginGoogle(@RequestParam(required = false) String redirectTo) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Thành công", authService.getGoogleUrl(redirectTo)));
    }

    @GetMapping(UrlMapping.OAUTH2_GOOGLE_CALLBACK)
    @Operation(summary = "API callback đăng nhập bằng google")
    RedirectView loginGoogleCallback(@RequestParam(required = false) String code,
                                     @RequestParam(required = false) String error,
                                     @RequestParam(required = false) String state) {
        return authService.loginGoogleCallback(code, error, state);
    }

    @GetMapping(UrlMapping.OAUTH2_FACEBOOK)
    @Operation(summary = "API đăng nhập bằng facebook")
    ResponseEntity<ApiResponse<String>> loginFacebook(@RequestParam(required = false) String redirectTo) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Thành công", authService.getFacebookUrl(redirectTo)));
    }

    @GetMapping(UrlMapping.OAUTH2_FACEBOOK_CALLBACK)
    @Operation(summary = "API callback đăng nhập bằng facebook")
    RedirectView loginFacebookCallback(@RequestParam(required = false) String code,
                                       @RequestParam(required = false) String error,
                                       @RequestParam(required = false) String state) {
        return authService.loginFacebookCallback(code, error, state);
    }

    @GetMapping(UrlMapping.OAUTH2_CALLBACK)
    @Operation(summary = "API callback đăng nhập bằng google hoặc facebook (hoặc các dịch vụ khác)")
    RedirectView loginCallback(@RequestParam(required = false) String type,
                               @RequestParam(required = false) String code,
                               @RequestParam(required = false) String error,
                               @RequestParam(required = false) String state) {
        return authService.loginOAuthCallback(type, code, error, state);
    }


    @GetMapping(UrlMapping.AUTHENTICATE_TOKEN)
    @Operation(summary = "API xác thực token")
    ResponseEntity<ApiResponse<UserResponse>> authenticateToken() {
        return ResponseEntity.ok(new ApiResponse<>(200, "Thành công", authService.authenticateToken()));
    }

    @PostMapping(UrlMapping.SEND_VERIFY_MAIL)
    @Operation(summary = "API gửi mail xác thực")
    ResponseEntity<?> sendVerifyMail(@RequestParam String email) {
        return ResponseEntity.ok(authService.sendEmailVerify(email));
    }

    @PostMapping(UrlMapping.VERIFY_EMAIL)
    @Operation(summary = "API xác thực email")
    ResponseEntity<?> verifyEmail(@RequestBody VerifyOtpRequest request) {
        return ResponseEntity.ok(authService.verifyEmail(request));
    }

    @PostMapping(UrlMapping.SEND_RESET_PASSWORD_MAIL)
    @Operation(summary = "API gửi mail reset mật khẩu")
    ResponseEntity<?> sendResetPasswordMail(@RequestParam String email) {
        return ResponseEntity.ok(authService.sendEmailResetPassword(email));
    }

    @PostMapping(UrlMapping.VERIFY_RESET_PASSWORD)
    @Operation(summary = "API xác thực reset mật khẩu")
    ResponseEntity<?> verifyResetPassword(@RequestBody VerifyOtpRequest request) {
        return ResponseEntity.ok(authService.verifyResetPassword(request));
    }

    @PostMapping(UrlMapping.RESET_PASSWORD)
    @Operation(summary = "API reset mật khẩu")
    ResponseEntity<?> resetPassword(@RequestBody ResetUserPasswordRequest request) {
        return ResponseEntity.ok(authService.resetPassword(request));
    }

    @PostMapping(UrlMapping.CHANGE_PASSWORD)
    @Operation(summary = "API đổi mật khẩu")
    ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(authService.changePassword(request));
    }

    @PostMapping(UrlMapping.UPDATE_USERNAME)
    @Operation(summary = "API cập nhật tên người dùng")
    ResponseEntity<?> updateUsername(@RequestParam String username) {
        return ResponseEntity.ok(authService.updateUsername(username));
    }

}
