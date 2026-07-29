package com.company.springbootstarter.exception;

import org.springframework.http.HttpStatus;

/** 资源未找到 (404) */
public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String resource, Long id) {
        super(
                String.format("%s not found with id: %d", resource, id),
                HttpStatus.NOT_FOUND,
                "RESOURCE_NOT_FOUND");
    }

    public ResourceNotFoundException(String resource, String field, String value) {
        super(
                String.format("%s not found with %s: %s", resource, field, value),
                HttpStatus.NOT_FOUND,
                "RESOURCE_NOT_FOUND");
    }
}
