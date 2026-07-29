package com.company.springbootstarter.controller;

import com.company.springbootstarter.dto.UserCreateRequest;
import com.company.springbootstarter.dto.UserResponse;
import com.company.springbootstarter.dto.UserUpdateRequest;
import com.company.springbootstarter.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired private UserService userService;

    /** 创建用户 POST /api/users */
    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    /** 查询用户列表 GET /api/users */
    @GetMapping
    public List<UserResponse> list() {
        return userService.getUsers();
    }

    /** 根据 ID 查询用户 GET /api/users/{id} */
    @GetMapping("/{id}")
    public UserResponse get(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    /** 更新用户 PUT /api/users/{id} */
    @PutMapping("/{id}")
    public UserResponse update(
            @PathVariable UUID id, @RequestBody @Valid UserUpdateRequest request) {

        return userService.updateUser(id, request);
    }

    /** 删除用户 DELETE /api/users/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
