package com.pacific.pacificbe.services.impl;


import com.google.api.gax.rpc.NotFoundException;
import com.pacific.pacificbe.dto.request.*;
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
import com.pacific.pacificbe.utils.JavaMail;
import com.pacific.pacificbe.utils.enums.UserRole;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public AuthenticationResponse loginUser(LoginRequest request) {
        return null;
    }

    @Override
    public UserRegisterResponse registerUser(UserRegisterRequest request) {
        return null;
    }

    @Override
    public UserResponse authenticateToken() {
        return null;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userMapper.toUserResponseList(userRepository.findAll());
    }

    @Override
    public String sendEmailVerify(String email) {
        return "";
    }

    @Override
    public String sendEmailResetPassword(String email) {
        return "";
    }

    @Override
    public boolean verifyEmail(VerifyOtpRequest request) {
        return false;
    }

    @Override
    public boolean verifyResetPassword(VerifyOtpRequest request) {
        return false;
    }

    @Override
    public boolean resetPassword(ResetUserPasswordRequest request) {
        return false;
    }


    @Override
    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        UserResponse userResponse = userMapper.toUserResponse(user);
        return userResponse;
    }


    @Override
    public UserResponse updateUser(String id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (request.getUsername() != null) user.setUsername(request.getUsername());
        if (request.getFirstname() != null) user.setFirstName(request.getFirstname());
        if (request.getLastname() != null) user.setLastName(request.getLastname());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getAddress() != null) user.setAddress(request.getAddress());
        if (request.getGender() != null) user.setGender(request.getGender());
        if (request.getBirthday() != null) user.setBirthday(request.getBirthday());
        if (request.getStatus() != null) user.setStatus(request.getStatus());
        if (request.getDeposit() != null) user.setDeposit(request.getDeposit());
        if (request.getAvatar() != null) user.setAvatarUrl(request.getAvatar());
        if (request.getRole() != null) user.setRole(request.getRole());

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateStatus(String id, UpdateStatusUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Nếu request có status mới, cập nhật theo request
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        } else {
        // Nếu không có status, tự động chuyển đổi giữa ACTIVE và INACTIVE
            user.setStatus("ACTIVE".equalsIgnoreCase(user.getStatus()) ? "INACTIVE" : "ACTIVE");
        }

        return userMapper.toUserResponse(userRepository.save(user));
    }

}
