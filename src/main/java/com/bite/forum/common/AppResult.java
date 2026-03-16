package com.bite.forum.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 业务统一响应结果封装类
 * 用于规范所有 Controller 层返回的数据格式
 *
 * @param <T> 业务数据泛型类型
 * @author 温悦成
 * @since 2026-03-16
 */
@Data
public class AppResult<T> {

    /**
     * 业务响应状态码
     */
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private long code;

    /**
     * 业务响应消息
     */
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private String message;

    /**
     * 业务数据对象
     */
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private T data;

    /**
     * 默认无参构造函数
     */
    public AppResult() {
    }

    /**
     * 基础构造函数
     *
     * @param code    状态码
     * @param message 响应消息
     */
    public AppResult(long code, String message) {
        this(code, message, null);
    }

    /**
     * 全参构造函数
     *
     * @param code    状态码
     * @param message 响应消息
     * @param data    响应数据
     */
    public AppResult(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 返回成功结果
     *
     * @return 成功响应
     */
    public static <T> AppResult<T> success() {
        return new AppResult<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }

    /**
     * 返回成功结果并携带数据
     *
     * @param data 业务数据
     * @param <T>  泛型类型
     * @return 成功响应
     */
    public static <T> AppResult<T> success(T data) {
        return new AppResult<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 返回成功结果并包含自定义消息
     *
     * @param message 自定义成功消息
     * @param <T>     泛型类型
     * @return 成功响应
     */
    public static <T> AppResult<T> success(String message) {
        return new AppResult<>(ResultCode.SUCCESS.getCode(), message);
    }

    /**
     * 返回成功结果并包含自定义消息和数据
     *
     * @param message 自定义消息
     * @param data    业务数据
     * @param <T>     泛型类型
     * @return 成功响应
     */
    public static <T> AppResult<T> success(String message, T data) {
        return new AppResult<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 返回通用失败结果
     *
     * @return 失败响应
     */
    public static <T> AppResult<T> failed() {
        return new AppResult<>(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMessage());
    }

    /**
     * 返回带自定义消息的失败结果
     *
     * @param message 错误描述消息
     * @param <T>     泛型类型
     * @return 失败响应
     */
    public static <T> AppResult<T> failed(String message) {
        return new AppResult<>(ResultCode.FAILED.getCode(), message);
    }

    /**
     * 返回带详细错误数据的失败结果
     *
     * @param data 异常或详细错误信息
     * @param <T>  泛型类型
     * @return 失败响应
     */
    public static <T> AppResult<T> failed(T data) {
        return new AppResult<>(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMessage(), data);
    }

    /**
     * 根据枚举定义的错误码返回失败结果
     *
     * @param resultCode 错误码枚举
     * @param <T>        泛型类型
     * @return 失败响应
     */
    public static <T> AppResult<T> failed(ResultCode resultCode) {
        return new AppResult<>(resultCode.getCode(), resultCode.getMessage());
    }
}
