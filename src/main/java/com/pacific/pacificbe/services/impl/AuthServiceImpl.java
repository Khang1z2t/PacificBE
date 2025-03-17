package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.LoginRequest;
import com.pacific.pacificbe.dto.request.ResetUserPasswordRequest;
import com.pacific.pacificbe.dto.request.UserRegisterRequest;
import com.pacific.pacificbe.dto.request.VerifyOtpRequest;
import com.pacific.pacificbe.dto.request.oauth2.GoogleTokenRequest;
import com.pacific.pacificbe.dto.response.AuthenticationResponse;
import com.pacific.pacificbe.dto.response.UserRegisterResponse;
import com.pacific.pacificbe.dto.response.UserResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.UserMapper;
import com.pacific.pacificbe.model.User;
import com.pacific.pacificbe.repository.UserRepository;
import com.pacific.pacificbe.repository.oauth2.GoogleClient;
import com.pacific.pacificbe.repository.oauth2.GoogleUserClient;
import com.pacific.pacificbe.services.AuthService;
import com.pacific.pacificbe.services.JwtService;
import com.pacific.pacificbe.services.OtpService;
import com.pacific.pacificbe.utils.IdUtil;
import com.pacific.pacificbe.utils.JavaMail;
import com.pacific.pacificbe.utils.UrlMapping;
import com.pacific.pacificbe.utils.enums.UserRole;
import com.pacific.pacificbe.utils.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final GoogleClient googleClient;
    private final GoogleUserClient googleUserClient;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final IdUtil idUtil;
    private final OtpService otpService;
    private final JavaMail javaMail;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    @Value("${oauth2.google.clientId}")
    private String googleClientId;
    @Value("${oauth2.google.clientSecret}")
    private String googleClientSecret;
    @Value("${oauth2.google.redirectUri}")
    private String googleRedirectUri;
    @Value("${oauth2.google.grantType}")
    private String googleGrantType;

    @Override
    public AuthenticationResponse loginUser(LoginRequest request) {
        // Mọi logic đều xử lý ở service
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getIdentifier(),
                            request.getPassword())
            );

            User user = (User) authentication.getPrincipal();

            if (!user.isActive()) throw new AppException(ErrorCode.USER_NOT_ACTIVE);

            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("uid", user.getId());
            extraClaims.put("role", user.getRole());

            var jwtToken = jwtService.generateToken(extraClaims, user);

            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .username(user.getUsername())
                    .role(user.getRole())
                    .build();
        } catch (AuthenticationException e) {
            throw new AppException(ErrorCode.INVALID_USERNAME_OR_PASSWORD);
        }
    }

    @Override
    public UserRegisterResponse registerUser(UserRegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        } else if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        } else if (userRepository.existsByUsernameAndEmail(request.getUsername(), request.getEmail())) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);
        user.setRole(UserRole.USER.toString());
        user.setAvatarUrl("https://drive.google.com/file/d/1_RTHRBB6K8yU2nsiSJU5LHU2d9FPbfvX/view?usp=drive_link");
        user = userRepository.save(user);

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("uid", user.getId());
        extraClaims.put("role", user.getRole());

        return UserRegisterResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .accessToken(jwtService
                        .generateToken(extraClaims, user))
                .build();
    }

    @Override
    public UserResponse authenticateToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (!user.isActive()) throw new AppException(ErrorCode.USER_NOT_ACTIVE);
        if (user.getUsername().equals(user.getEmail().split("@")[0])) {
            if (!user.getStatus().equals(UserStatus.REQUIRE_USERNAME_PASSWORD_CHANGE.toString())) {
                user.setStatus(UserStatus.REQUIRE_USERNAME_CHANGE.toString());
            }
        } else if (user.getStatus().equals(UserStatus.REQUIRE_USERNAME_PASSWORD_CHANGE.toString())) {
            user.setStatus(UserStatus.REQUIRE_PASSWORD_CHANGE.toString());
        } else {
            user.setStatus(UserStatus.ACTIVE.toString());
        }
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    public String sendEmailVerify(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        String otp = otpService.generateOtp(email);
        javaMail.sendMailVerify(user, otp);
        return "Gửi email thành công";
    }

    @Override
    public String sendEmailResetPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        String otp = otpService.generateOtp(email);
        javaMail.sendMailForgotPassword(user, otp);
        return "Gửi email thành công";
    }


    @Override
    public boolean verifyEmail(VerifyOtpRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (otpService.verifyOtp(request.getEmail(), request.getOtp())) {
            user.setEmailVerified(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }


    @Override
    public boolean verifyResetPassword(VerifyOtpRequest request) {
        return otpService.verifyOtp(request.getEmail(), request.getOtp());
    }

    @Override
    public boolean resetPassword(ResetUserPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (request.getNewPassword().equals(request.getConfirmPassword())) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            user.setStatus(UserStatus.ACTIVE.toString());
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public String getGoogleUrl() {
        return "https://accounts.google.com/o/oauth2/auth"
                + "?client_id=" + googleClientId
                + "&redirect_uri=" + googleRedirectUri
                + "&response_type=code"
                + "&scope=openid%20profile%20email";
    }

    @Override
    public RedirectView loginGoogleCallback(String code, String error) {
        if (error != null) {
            String url = UrlMapping.GOOGLE_REDIRECT + "?error=" + error;
            return new RedirectView(url);
        }
        var response = googleClient.exchangeToken(GoogleTokenRequest.builder()
                .clientId(googleClientId)
                .clientSecret(googleClientSecret)
                .redirectUri(googleRedirectUri)
                .grantType(googleGrantType)
                .code(code)
                .build());

        var userInfo = googleUserClient.getUserInfo("Bearer " + response.getAccessToken());
        var user = userRepository.findByEmail(userInfo.getEmail()).orElseGet(
                () -> userRepository.save(User.builder()
                        .email(userInfo.getEmail())
                        .username(userInfo.getEmail().split("@")[0])
                        .firstName(userInfo.getGivenName())
                        .lastName(userInfo.getFamilyName())
                        .avatarUrl(idUtil.getIdAvatar(userInfo.getPicture()))
                        .status(UserStatus.REQUIRE_USERNAME_PASSWORD_CHANGE.toString())
                        .password(passwordEncoder.encode(idUtil.generateRandomPassword()))
                        .role(UserRole.USER.toString())
                        .emailVerified(true)
                        .active(true)
                        .build()));

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("provider", "google");
        extraClaims.put("email", user.getEmail());
        extraClaims.put("status", "require_update_username_and_password");

        var accessToken = jwtService.generateToken(extraClaims, user);

        String url = UrlMapping.GOOGLE_REDIRECT + "?access_token=" + accessToken + "&refresh_token=" + accessToken;
        return new RedirectView(url);
    }
}
