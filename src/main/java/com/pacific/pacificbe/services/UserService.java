package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.*;
import com.pacific.pacificbe.dto.response.AuthenticationResponse;
import com.pacific.pacificbe.dto.response.TourResponse;
import com.pacific.pacificbe.dto.response.UserRegisterResponse;
import com.pacific.pacificbe.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> getAllUsers();

    UserResponse createUser(UserRegisterRequest request);

    UserResponse getUserById(String id);

    UserResponse updateUser(String id, UpdateUserRequest request);

    UserResponse updateStatus(String id, UpdateStatusUserRequest request);
}
