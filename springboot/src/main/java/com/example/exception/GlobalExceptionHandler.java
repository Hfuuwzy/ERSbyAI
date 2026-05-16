package com.example.exception;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.example.common.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.validation.ConstraintViolationException;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice("com.example.controller")
public class GlobalExceptionHandler {

    private static final Log log = LogFactory.get();

    /**
     * 通用异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        log.error("系统异常：", e);
        return Result.error("500", "系统异常，请稍后重试");
    }

    /**
     * 自定义异常
     */
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Result error(CustomException e) {
        return Result.error(e.getCode(), e.getMsg());
    }

    /**
     * 参数校验失败 - @Valid @RequestBody
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("参数校验失败：{}", message);
        return Result.error("400", "参数错误：" + message);
    }

    /**
     * 参数校验失败 - @Validated @RequestParam
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Result handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
        log.warn("参数校验失败：{}", message);
        return Result.error("400", "参数错误：" + message);
    }

    /**
     * 类型转换失败
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public Result handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        String message = String.format("参数'%s'类型错误，期望类型：%s",
                e.getName(), e.getRequiredType().getSimpleName());
        log.warn("类型转换失败：{}", message);
        return Result.error("400", message);
    }

    /**
     * 缺少请求参数
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public Result handleMissingParam(MissingServletRequestParameterException e) {
        String message = String.format("缺少必要参数：%s（%s）",
                e.getParameterName(), e.getParameterType());
        log.warn("缺少参数：{}", message);
        return Result.error("400", message);
    }

    /**
     * 找不到资源
     */
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseBody
    public Result handleNotFound(NoSuchElementException e) {
        log.warn("资源不存在：{}", e.getMessage());
        return Result.error("404", "资源不存在");
    }

    /**
     * 非法参数
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public Result handleIllegalArgument(IllegalArgumentException e) {
        log.warn("非法参数：{}", e.getMessage());
        return Result.error("400", e.getMessage());
    }

    /**
     * 请求方法不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Result handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        String message = String.format("不支持%s请求，支持的请求：%s",
                e.getMethod(), String.join(", ", e.getSupportedMethods()));
        log.warn("请求方法不支持：{}", message);
        return Result.error("405", message);
    }

    /**
     * 404 页面未找到
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Result handleNoHandlerFound(NoHandlerFoundException e) {
        log.warn("接口不存在：{} {}", e.getHttpMethod(), e.getRequestURL());
        return Result.error("404", "接口不存在：" + e.getRequestURL());
    }

    /**
     * 绑定异常（@ModelAttribute 参数错误）
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Result handleBindException(BindException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("参数绑定失败：{}", message);
        return Result.error("400", "参数错误：" + message);
    }
}
