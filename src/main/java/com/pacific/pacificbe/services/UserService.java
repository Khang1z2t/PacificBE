package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.LoginRequest;
import com.pacific.pacificbe.dto.request.UserRegisterRequest;
import com.pacific.pacificbe.dto.request.VerifyEmailRequest;
import com.pacific.pacificbe.dto.response.AuthenticationResponse;
import com.pacific.pacificbe.dto.response.UserRegisterResponse;
import com.pacific.pacificbe.dto.response.UserResponse;

public interface UserService {
    AuthenticationResponse loginUser(LoginRequest request);

    UserRegisterResponse registerUser(UserRegisterRequest request);

    UserResponse authenticateToken();

    Boolean verifyEmail(VerifyEmailRequest request);
}
