package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.request.UserRegisterRequest;
import com.pacific.pacificbe.dto.response.UserResponse;
import com.pacific.pacificbe.model.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserMapper {
    ModelMapper modelMapper;

    public UserResponse toUserResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }

    public User toUser(UserResponse userResponse) {
        return modelMapper.map(userResponse, User.class);
    }

    public User toUser(UserRegisterRequest userRegisterRequest) {
        return modelMapper.map(userRegisterRequest, User.class);
    }

    public List<UserResponse> toUserResponseList(List<User> users) {
        return users.stream()
                .map(this::toUserResponse)
                .toList();
    }
}
