package com.pacific.pacificbe.services.impl;


import com.pacific.pacificbe.dto.request.*;
import com.pacific.pacificbe.dto.response.UserResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.UserMapper;
import com.pacific.pacificbe.model.User;
import com.pacific.pacificbe.repository.UserRepository;
import com.pacific.pacificbe.services.GoogleDriveService;
import com.pacific.pacificbe.services.UserService;
import com.pacific.pacificbe.utils.AuthUtils;
import com.pacific.pacificbe.utils.enums.FolderType;
import com.pacific.pacificbe.utils.enums.UserRole;
import com.pacific.pacificbe.utils.enums.UserStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final GoogleDriveService googleDriveService;

    @Override
    public List<UserResponse> getAllUsers() {
        return userMapper.toUserResponseList(userRepository.findAll());
    }

    @Override
    public UserResponse createUser(UserRegisterRequest request) {
        return null;
    }


    @Override
    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
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
        if (request.getAvatar() != null) user.setAvatarUrl(request.getAvatar());

        if (EnumUtils.isValidEnum(UserStatus.class, request.getStatus().toUpperCase())) {
            user.setStatus(request.getStatus().toUpperCase());
        } else {
            throw new AppException(ErrorCode.USER_STATUS_INVALID);
        }
        if (EnumUtils.isValidEnum(UserRole.class, request.getRole().toUpperCase())) {
            user.setRole(request.getRole().toUpperCase());
        } else {
            throw new AppException(ErrorCode.USER_ROLE_INVALID);
        }

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
            user.setStatus(UserStatus.ACTIVE.toString().equalsIgnoreCase(
                    user.getStatus()) ? UserStatus.INACTIVE.toString() : UserStatus.ACTIVE.toString());
        }

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateProfile(UpdateProfileRequest request, MultipartFile avatar) {
        String userId = AuthUtils.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!user.getEmail().equals(request.getEmail())) {
            user.setEmailVerified(false);
        }
        if (!user.getPhone().equals(request.getPhone())) {
            user.setPhoneVerified(false);
        }
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setGender(request.getGender());
        user.setBirthday(request.getBirthday());
        if (avatar != null && !avatar.isEmpty()) {
            user.setAvatarUrl(googleDriveService.uploadImageToDrive(avatar, FolderType.AVATAR));
        }
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

}
