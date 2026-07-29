package com.company.springbootstarter.service.impl;

import com.company.springbootstarter.dto.UserCreateRequest;
import com.company.springbootstarter.dto.UserResponse;
import com.company.springbootstarter.dto.UserUpdateRequest;
import com.company.springbootstarter.entity.User;
import com.company.springbootstarter.exception.DuplicateResourceException;
import com.company.springbootstarter.exception.ResourceNotFoundException;
import com.company.springbootstarter.repository.UserRepository;
import com.company.springbootstarter.service.UserService;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired private UserRepository userRepository;

    @Override
    public UserResponse createUser(UserCreateRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("用户名已存在: " + request.username());
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("邮箱已存在: " + request.email());
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());

        User saved = userRepository.save(user);
        log.info("用户创建成功: id={}", saved.getId());

        return toResponse(user);
    }

    @Override
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public UserResponse getUserById(UUID id) {
        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("用户", id));
        return toResponse(user);
    }

    @Override
    public UserResponse updateUser(UUID id, UserUpdateRequest request) {
        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("用户", id));

        if (StringUtils.hasText(request.username())) {
            user.setUsername(request.username());
        }
        if (StringUtils.hasText(request.email())) {
            user.setEmail(request.email());
        }

        User updated = userRepository.save(user);
        log.info("用户更新成功: id={}", updated.getId());

        return toResponse(user);
    }

    @Override
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("用户", id);
        }

        userRepository.deleteById(id);
        log.info("用户删除成功: id={}", id);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail());
    }
}
