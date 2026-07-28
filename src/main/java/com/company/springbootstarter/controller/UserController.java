package com.company.springbootstarter.controller;


import com.company.springbootstarter.dto.UserCreateRequest;
import com.company.springbootstarter.dto.UserResponse;
import com.company.springbootstarter.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(request));
    }


    @GetMapping("/{id}")
    public UserResponse get(@PathVariable UUID id) {
        return userService.getUserById(id);
    }


}
