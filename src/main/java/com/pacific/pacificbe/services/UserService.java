package com.pacific.pacificbe.services;

import com.google.firebase.auth.multitenancy.Tenant;
import com.pacific.pacificbe.dto.request.*;
import com.pacific.pacificbe.dto.response.AuthenticationResponse;
import com.pacific.pacificbe.dto.response.TourResponse;
import com.pacific.pacificbe.dto.response.UserRegisterResponse;
import com.pacific.pacificbe.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    AuthenticationResponse loginUser(LoginRequest request);

    UserRegisterResponse registerUser(UserRegisterRequest request);

    UserResponse authenticateToken();

    List<UserResponse> getAllUsers();

    String sendEmailVerify(String email);

    String sendEmailResetPassword(String email);

    boolean verifyEmail(VerifyOtpRequest request);

    boolean verifyResetPassword(VerifyOtpRequest request);

    boolean resetPassword(ResetUserPasswordRequest request);

    UserResponse getUserById(String id);

    UserResponse updateUser(String id, UpdateUserRequest request);

    UserResponse updateStatus(String id, UpdateStatusUserRequest request);

}
