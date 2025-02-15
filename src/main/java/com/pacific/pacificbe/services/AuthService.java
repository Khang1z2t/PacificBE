package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.LoginRequest;
import com.pacific.pacificbe.dto.request.UserRegisterRequest;
import com.pacific.pacificbe.dto.response.AuthenticationResponse;
import com.pacific.pacificbe.dto.response.UserRegisterResponse;

public interface AuthService {
    AuthenticationResponse loginUser(LoginRequest request);

    UserRegisterResponse registerUser(UserRegisterRequest request);
}
