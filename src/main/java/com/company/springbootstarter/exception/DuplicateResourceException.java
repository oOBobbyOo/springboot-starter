package com.company.springbootstarter.exception;

import org.springframework.http.HttpStatus;

/** 资源重复/冲突 (409) */
public class DuplicateResourceException extends BusinessException {
    public DuplicateResourceException(String message) {
        super(message, HttpStatus.CONFLICT, "DUPLICATE_RESOURCE");
    }
}
