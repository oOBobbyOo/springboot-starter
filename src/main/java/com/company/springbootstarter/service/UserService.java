package com.company.springbootstarter.service;

import com.company.springbootstarter.dto.UserCreateRequest;
import com.company.springbootstarter.dto.UserResponse;
import com.company.springbootstarter.dto.UserUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse createUser(UserCreateRequest request);

    List<UserResponse> getUsers();

    UserResponse getUserById(UUID id);

    UserResponse updateUser(UUID id, UserUpdateRequest request);

    void deleteUser(UUID id);
}
