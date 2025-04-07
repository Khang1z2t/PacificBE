package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.*;
import com.pacific.pacificbe.dto.response.TopBookedUsersResponse;
import com.pacific.pacificbe.dto.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    List<UserResponse> getAllUsers();

    UserResponse createUser(UserRegisterRequest request);

    UserResponse getUserById(String id);

    UserResponse updateUser(String id, UpdateUserRequest request);

    UserResponse updateStatus(String id, boolean active);

    UserResponse updateProfile(UpdateProfileRequest request, MultipartFile avatar);

    Long getTotalUser();

    List<TopBookedUsersResponse> getTopBookedUsers(int limit);
}
