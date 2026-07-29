package com.company.springbootstarter.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 所有业务异常的基类
 *
 * <p>子类只需指定 HTTP 状态码和错误消息即可
 */
@Getter
public class BusinessException extends RuntimeException {
    private final HttpStatus status;
    private final String errorCode;

    protected BusinessException(String message, HttpStatus status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }
}
