package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.LoginRequest;
import com.pacific.pacificbe.dto.request.ResetUserPasswordRequest;
import com.pacific.pacificbe.dto.request.UserRegisterRequest;
import com.pacific.pacificbe.dto.request.VerifyOtpRequest;
import com.pacific.pacificbe.dto.response.AuthenticationResponse;
import com.pacific.pacificbe.dto.response.UserRegisterResponse;
import com.pacific.pacificbe.dto.response.UserResponse;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

public interface AuthService {

    AuthenticationResponse loginUser(LoginRequest request);

    UserRegisterResponse registerUser(UserRegisterRequest request);

    UserResponse authenticateToken();


    String sendEmailVerify(String email);

    String sendEmailResetPassword(String email);

    boolean verifyEmail(VerifyOtpRequest request);

    boolean verifyResetPassword(VerifyOtpRequest request);

    boolean resetPassword(ResetUserPasswordRequest request);

    String getGoogleUrl();

    RedirectView loginGoogleCallback(String code, String error);

}
