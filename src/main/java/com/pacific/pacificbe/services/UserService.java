package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.*;
import com.pacific.pacificbe.dto.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    List<UserResponse> getAllUsers();

    UserResponse createUser(UserRegisterRequest request);

    UserResponse getUserById(String id);

    UserResponse updateUser(String id, UpdateUserRequest request);

    UserResponse updateStatus(String id, UpdateStatusUserRequest request);

    UserResponse updateProfile(UpdateProfileRequest request, MultipartFile avatar);
}
