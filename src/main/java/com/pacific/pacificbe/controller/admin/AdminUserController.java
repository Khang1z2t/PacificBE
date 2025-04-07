//package com.pacific.pacificbe.controller.admin;
//
//import com.pacific.pacificbe.dto.ApiResponse;
//import com.pacific.pacificbe.dto.request.UpdateStatusUserRequest;
//import com.pacific.pacificbe.dto.request.UpdateUserRequest;
//import com.pacific.pacificbe.dto.response.UserResponse;
//import com.pacific.pacificbe.services.UserService;
//import com.pacific.pacificbe.utils.UrlMapping;
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/admin/user")
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class AdminUserController {
//    UserService userService;
//
//
//    @GetMapping(UrlMapping.GET_ALL_USERS)
//    @Operation(summary = "Lấy danh sách tất cả người dùng")
//    ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
//        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy danh sách tất cả người dùng thành công", userService.getAllUsers()));
//    }
//
//
//    @GetMapping(UrlMapping.GET_USER_BY_ID)
//    @Operation(summary = "Lấy thông tin người dùng theo ID")
//    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable String id) {
//        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy thông tin thành công", userService.getUserById(id)));
//    }
//
//
//    @PutMapping(UrlMapping.UPDATE_USER)
//    @Operation(summary = "Cập nhật thông tin người dùng")
//    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable String id, @RequestBody UpdateUserRequest request) {
//        return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật thông tin thành công", userService.updateUser(id, request)));
//    }
//
//
////    @PatchMapping(UrlMapping.UPDATE_STATUS_USER)
////    @Operation(summary = "Cập nhật trạng thái người dùng")
////    public ResponseEntity<ApiResponse<UserResponse>> updateStatusUser(
////            @PathVariable String id,
////            @RequestBody UpdateStatusUserRequest request) {
////        return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật trạng thái thành công", userService.updateStatus(id, request)));
////    }
//}
