package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.LoginRequest;
import com.pacific.pacificbe.dto.response.AuthenticationResponse;
//import com.pacific.pacificbe.mapper.UserMapper;
import com.pacific.pacificbe.repository.UserRepository;
import com.pacific.pacificbe.services.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
//    UserMapper userMapper;


}
