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

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public List<UserResponse> getAllUsers() {
        return userMapper.toUserResponseList(userRepository.findAll());
    }


}
