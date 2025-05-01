package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.*;
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

    String getGoogleUrl(String redirectTo);

    String getFacebookUrl(String redirectTo);

    RedirectView loginOAuthCallback(String type, String code, String error, String state);

    boolean changePassword(ChangePasswordRequest request);

    boolean updateUsername(String username);

    boolean updateUsernameAndPassword(UpdateUsernameAndPassRequest request);

}
