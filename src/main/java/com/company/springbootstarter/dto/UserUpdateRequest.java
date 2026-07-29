package com.company.springbootstarter.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Size(min = 2, max = 50, message = "用户名长度必须在 2-50 之间") String username,
        @Email(message = "邮箱格式不正确") String email) {}
