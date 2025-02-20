package com.pacific.pacificbe.services.impl;


import com.pacific.pacificbe.dto.request.LoginRequest;
import com.pacific.pacificbe.dto.request.ResetUserPasswordRequest;
import com.pacific.pacificbe.dto.request.UserRegisterRequest;
import com.pacific.pacificbe.dto.request.VerifyOtpRequest;
import com.pacific.pacificbe.dto.response.AuthenticationResponse;
import com.pacific.pacificbe.dto.response.UserRegisterResponse;
import com.pacific.pacificbe.dto.response.UserResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.UserMapper;
import com.pacific.pacificbe.model.User;
import com.pacific.pacificbe.repository.UserRepository;
import com.pacific.pacificbe.services.JwtService;
import com.pacific.pacificbe.services.OtpService;
import com.pacific.pacificbe.services.UserService;
import com.pacific.pacificbe.utils.AuthenUtils;
import com.pacific.pacificbe.utils.JavaMail;
import com.pacific.pacificbe.utils.enums.UserRole;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.Util;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    AuthenticationManager authenticationManager;
    JwtService jwtService;
    JavaMail javaMail;
    OtpService otpService;

    @Override
    public AuthenticationResponse loginUser(LoginRequest request) {
        // Mọi logic đều xử lý ở service
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new AppException(ErrorCode.INVALID_USERNAME_OR_PASSWORD);
        }
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
        return userMapper.toUserResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userMapper.toUserResponseList(userRepository.findAll());
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
        String userId = AuthenUtils.getCurrentUserId();
        if (Objects.isNull(userId)) {
            throw new AppException(ErrorCode.NEED_LOGIN);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (request.getNewPassword().equals(request.getConfirmPassword())) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
            return true;
        }
        return false;
    }


}
