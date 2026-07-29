package com.company.springbootstarter.exception;

import org.springframework.http.HttpStatus;

/** 未授权 (401) */
public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
    }
}
