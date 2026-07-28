package com.company.springbootstarter.service.impl;

import com.company.springbootstarter.dto.UserCreateRequest;
import com.company.springbootstarter.dto.UserResponse;
import com.company.springbootstarter.dto.UserUpdateRequest;
import com.company.springbootstarter.entity.User;
import com.company.springbootstarter.repository.UserRepository;
import com.company.springbootstarter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserResponse createUser(UserCreateRequest request) {

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());

        userRepository.save(user);

        return toResponse(user);
    }


    @Override
    public UserResponse getUserById(UUID id) {
        System.out.println("正在查询 UUID: " + id);

        User user = userRepository.findById(id).orElseThrow(() -> {
                System.err.println(id);
                return new RuntimeException("用户不存在: " + id);
        });
        return toResponse(user);
    }

    @Override
    public UserResponse updateUser(UUID id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(request.username());
        user.setEmail(request.email());

        userRepository.save(user);

        return toResponse(user);
    }

    @Override
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}
