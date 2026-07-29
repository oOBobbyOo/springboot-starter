package com.company.springbootstarter.exception;

import java.util.UUID;
import org.springframework.http.HttpStatus;

/** 资源未找到 (404) */
public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String resource, Long id) {
        super(
                String.format("%s 资源不存在，ID: %d", resource, id),
                HttpStatus.NOT_FOUND,
                "RESOURCE_NOT_FOUND");
    }

    public ResourceNotFoundException(String resource, UUID id) {
        super(
                String.format("%s 资源不存在，ID: %s", resource, id),
                HttpStatus.NOT_FOUND,
                "RESOURCE_NOT_FOUND");
    }

    public ResourceNotFoundException(String resource, String field, String value) {
        super(
                String.format("%s 资源不存在，%s: %s", resource, field, value),
                HttpStatus.NOT_FOUND,
                "RESOURCE_NOT_FOUND");
    }
}
