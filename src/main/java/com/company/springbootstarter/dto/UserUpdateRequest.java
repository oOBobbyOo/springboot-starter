package com.company.springbootstarter.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequest(

        @NotBlank
        String username,

        @Email
        @NotBlank
        String email
) {
}