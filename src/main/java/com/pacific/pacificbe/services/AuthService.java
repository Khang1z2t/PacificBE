package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.LoginRequest;
import com.pacific.pacificbe.dto.response.AuthenticationResponse;

public interface AuthService {
    AuthenticationResponse loginUser(LoginRequest request);
}
