package com.company.springbootstarter.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 全局异常处理器
 *
 * <p>遵循 RFC 7807 标准，所有错误响应统一为 ProblemDetail 格式。<br>
 * 优先级：具体异常 > 通用异常 > 兜底异常
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ==================== 自定义业务异常 ====================

    /**
     * 处理所有继承自 BusinessException 的自定义异常
     *
     * <p>包括: ResourceNotFoundException, DuplicateResourceException, UnauthorizedException
     */
    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusinessException(BusinessException ex, HttpServletRequest request) {
        log.warn(
                "业务异常 [{}]: {} | URI: {}",
                ex.getErrorCode(),
                ex.getMessage(),
                request.getRequestURI());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(ex.getStatus(), ex.getMessage());
        problem.setTitle(ex.getStatus().getReasonPhrase());
        problem.setType(
                URI.create("https://api.example.com/errors/" + ex.getErrorCode().toLowerCase()));
        problem.setProperty("errorCode", ex.getErrorCode());
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("path", request.getRequestURI());

        return problem;
    }

    // ==================== 参数校验异常 ====================

    /**
     * 处理 @Valid / @Validated 触发的参数校验失败 (400)
     *
     * <p>示例触发场景: <br>
     * - @NotBlank 字段为空 <br>
     * - @Email 格式不正确 <br>
     * - @Size 长度超限
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        // 收集所有字段的校验错误
        Map<String, String> fieldErrors =
                ex.getBindingResult().getFieldErrors().stream()
                        .collect(
                                Collectors.toMap(
                                        FieldError::getField,
                                        error ->
                                                error.getDefaultMessage() != null
                                                        ? error.getDefaultMessage()
                                                        : "校验失败",
                                        (existing, replacement) -> existing));

        log.warn("参数校验失败 | URI: {} | 错误字段: {}", request.getRequestURI(), fieldErrors.keySet());

        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "请求参数校验失败，请检查输入");
        problem.setTitle("Validation Error");
        problem.setType(URI.create("https://api.example.com/errors/validation"));
        problem.setProperty("errorCode", "VALIDATION_FAILED");
        problem.setProperty("fieldErrors", fieldErrors);
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("path", request.getRequestURI());

        return problem;
    }

    /**
     * 处理缺少必需的请求参数 (400)
     *
     * <p>示例: GET /api/users?name= 缺少 @RequestParam 标注的必填参数
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ProblemDetail handleMissingParam(
            MissingServletRequestParameterException ex, HttpServletRequest request) {

        log.warn("缺少请求参数: {} | URI: {}", ex.getParameterName(), request.getRequestURI());

        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.BAD_REQUEST,
                        String.format(
                                "缺少必需的请求参数: '%s' (类型: %s)",
                                ex.getParameterName(), ex.getParameterType()));
        problem.setTitle("Missing Parameter");
        problem.setType(URI.create("https://api.example.com/errors/missing-parameter"));
        problem.setProperty("errorCode", "MISSING_PARAMETER");
        problem.setProperty("parameterName", ex.getParameterName());
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }

    /**
     * 处理路径变量/请求参数类型不匹配 (400)
     *
     * <p>示例: GET /api/users/abc (id 应该是 Long，但传了字符串 "abc")
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        String requiredType =
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
        log.warn(
                "参数类型不匹配: {} 期望 {} 但收到 '{}' | URI: {}",
                ex.getName(),
                requiredType,
                ex.getValue(),
                request.getRequestURI());

        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.BAD_REQUEST,
                        String.format("参数 '%s' 类型错误，期望 %s 类型", ex.getName(), requiredType));
        problem.setTitle("Type Mismatch");
        problem.setType(URI.create("https://api.example.com/errors/type-mismatch"));
        problem.setProperty("errorCode", "TYPE_MISMATCH");
        problem.setProperty("parameterName", ex.getName());
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }

    // ==================== 请求格式异常 ====================

    /**
     * 处理 JSON 请求体解析失败 (400)
     *
     * <p>示例: 发送了格式错误的 JSON，或缺少必需的 JSON 字段
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        log.warn(
                "请求体解析失败 | URI: {} | 原因: {}",
                request.getRequestURI(),
                ex.getMostSpecificCause().getMessage());

        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "请求体 JSON 格式错误或无法解析");
        problem.setTitle("Malformed Request");
        problem.setType(URI.create("https://api.example.com/errors/malformed-request"));
        problem.setProperty("errorCode", "MALFORMED_JSON");
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }

    // ==================== HTTP 方法异常 ====================

    /**
     * 处理 HTTP 请求方法不支持 (405)
     *
     * <p>示例: 接口只支持 POST，但客户端发送了 GET 请求
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ProblemDetail handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {

        log.warn("不支持的请求方法: {} | URI: {}", ex.getMethod(), request.getRequestURI());

        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.METHOD_NOT_ALLOWED,
                        String.format(
                                "不支持 %s 方法，允许的方法: %s",
                                ex.getMethod(), ex.getSupportedHttpMethods()));
        problem.setTitle("Method Not Allowed");
        problem.setType(URI.create("https://api.example.com/errors/method-not-allowed"));
        problem.setProperty("errorCode", "METHOD_NOT_ALLOWED");
        problem.setProperty("supportedMethods", ex.getSupportedHttpMethods());
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }

    /**
     * 处理静态资源/路径未找到 (404)
     *
     * <p>Spring Boot 3.2+ 引入 NoResourceFoundException 替代旧的 NoHandlerFoundException
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ProblemDetail handleNoResourceFound(
            NoResourceFoundException ex, HttpServletRequest request) {

        log.warn("资源未找到 | URI: {}", request.getRequestURI());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "请求的路径不存在");
        problem.setTitle("Not Found");
        problem.setType(URI.create("https://api.example.com/errors/not-found"));
        problem.setProperty("errorCode", "PATH_NOT_FOUND");
        problem.setProperty("path", request.getRequestURI());
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }

    // ==================== 权限异常 ====================

    /**
     * 处理 Spring Security 的访问拒绝异常 (403)
     *
     * <p>如果项目未引入 Spring Security，可以删除此方法
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {

        log.warn("访问被拒绝 | URI: {} | 原因: {}", request.getRequestURI(), ex.getMessage());

        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "您没有权限访问此资源");
        problem.setTitle("Forbidden");
        problem.setType(URI.create("https://api.example.com/errors/forbidden"));
        problem.setProperty("errorCode", "ACCESS_DENIED");
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }

    // ==================== 全局兜底异常 ====================

    /**
     * 处理所有未被上述方法捕获的异常 (500)
     *
     * <p>⚠️ 生产环境中绝不向客户端暴露内部堆栈信息！<br>
     * 只返回通用错误消息，详细错误记录到日志中供排查。
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleAllUncaughtExceptions(Exception ex, HttpServletRequest request) {

        // 🔴 记录完整堆栈，方便运维排查
        log.error(
                "未捕获的服务器内部错误 | URI: {} | 异常类型: {}",
                request.getRequestURI(),
                ex.getClass().getName(),
                ex);

        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        /* ⚠️ 不暴露 ex.getMessage() */
                        "服务器内部错误，请联系管理员");
        problem.setTitle("Internal Server Error");
        problem.setType(URI.create("https://api.example.com/errors/internal-error"));
        problem.setProperty("errorCode", "INTERNAL_ERROR");
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("path", request.getRequestURI());

        return problem;
    }
}
