package com.company.springbootstarter.service;

import com.company.springbootstarter.dto.UserCreateRequest;
import com.company.springbootstarter.dto.UserResponse;

import java.util.UUID;

public interface UserService {

    UserResponse createUser(UserCreateRequest request);

    UserResponse getUserById(UUID id);
}
