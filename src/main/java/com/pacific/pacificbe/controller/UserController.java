package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.request.*;
import com.pacific.pacificbe.dto.response.TopBookedUsersResponse;
import com.pacific.pacificbe.dto.response.UserResponse;
import com.pacific.pacificbe.model.User;
import com.pacific.pacificbe.repository.UserRepository;
import com.pacific.pacificbe.services.UserService;
import com.pacific.pacificbe.utils.IdUtil;
import com.pacific.pacificbe.utils.MailService;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(UrlMapping.USERS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    private final MailService mailService;
    private final UserRepository userRepository;
    private final IdUtil idUtil;


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(UrlMapping.GET_ALL_USERS)
    @Operation(summary = "API lấy danh sách tất cả người dùng")
    ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(new ApiResponse<>(200, "Thành công", userService.getAllUsers()));
    }

    @GetMapping(UrlMapping.GET_USER_BY_ID)
    @Operation(summary = "API lấy thông tin người dùng theo ID")
    ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Thành công", userService.getUserById(id)));
    }

    @PostMapping(value = UrlMapping.UPDATE_PROFILE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "API cập nhật thông tin người dùng")
    ResponseEntity<ApiResponse<UserResponse>> updateProfile(@ModelAttribute UpdateProfileRequest request,
                                                            @RequestParam(required = false) MultipartFile avatar) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật thành công", userService.updateProfile(request, avatar)));
    }

    //    test controller
    @GetMapping("test-mail")
    @Operation(summary = "API test gửi mail")
    ResponseEntity<?> testMail(@RequestParam String email, @RequestParam String subject, @RequestParam String content) {
        mailService.sendEmail(email, subject, content);
        return ResponseEntity.ok("OK");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/test-user")
    @Operation(summary = "API test tạo user")
    ResponseEntity<?> testaddUser() {
        userRepository.save(User.builder().username(idUtil.generateRandomID()).password("12345678").build());
        return ResponseEntity.ok("OK");
    }

    @PatchMapping(UrlMapping.UPDATE_STATUS_USER)
    @Operation(summary = "API cập nhật trạng thái người dùng")
    ResponseEntity<ApiResponse<UserResponse>> updateStatusUser(
            @PathVariable String id,
            @RequestParam boolean active) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật trạng thái thành công", userService.updateStatus(id, active)));
    }

    @GetMapping(UrlMapping.COUNT_ALL_USERS)
    @Operation(summary = "API đếm tổng số người dùng")
    ResponseEntity<ApiResponse<Long>> getTotalUser() {
        return ResponseEntity.ok(new ApiResponse<>(200, "Thành công", userService.getTotalUser()));
    }

    @GetMapping(UrlMapping.TOP_BOOKED_USERS)
    @Operation(summary = "API lấy danh sách người dùng đã đặt tour nhiều nhất")
    ResponseEntity<ApiResponse<List<TopBookedUsersResponse>>> getTopBookedUsers(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Thành công", userService.getTopBookedUsers(limit)));
    }
}
