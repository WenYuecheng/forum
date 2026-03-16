package com.bite.forum.common;

/**
 * 业务响应码枚举
 * 定义系统中通用的返回码及其描述信息
 *
 * @author 温悦成
 * @since 2026-03-16
 */
public enum ResultCode {

    /**
     * 操作成功
     */
    SUCCESS(0, "成功"),

    /**
     * 基础操作失败
     */
    FAILED(1000, "操作失败"),

    /**
     * 未授权/未登录
     */
    FAILED_UNAUTHORIZED(1001, "未授权"),

    /**
     * 参数校验失败
     */
    FAILED_PARAMS_VALIDATE(1002, "参数校验失败"),

    /**
     * 禁止访问/权限不足
     */
    FAILED_FORBIDDEN(1003, "禁止访问"),

    /**
     * 新增数据失败
     */
    FAILED_CREATE(1004, "新增失败"),

    /**
     * 资源或记录不存在
     */
    FAILED_NOT_EXISTS(1005, "资源不存在"),

    /**
     * 用户已存在（注册冲突）
     */
    FAILED_USER_EXISTS(1101, "用户已存在"),

    /**
     * 用户不存在（查询或登录失败）
     */
    FAILED_USER_NOT_EXISTS(1102, "用户不存在"),

    /**
     * 登录凭证（用户名或密码）错误
     */
    FAILED_LOGIN(1103, "用户名或密码错误"),

    /**
     * 账号被系统封禁
     */
    FAILED_USER_BANNED(1104, "您已被禁言, 请联系管理员, 并重新登录."),

    /**
     * 注册或重置时两次密码输入不一致
     */
    FAILED_TWO_PWD_NOT_SAME(1105, "两次输入的密码不一致"),

    /**
     * 服务器内部服务错误
     */
    ERROR_SERVICES(2000, "服务器内部错误"),

    /**
     * 关键对象为空错误
     */
    ERROR_IS_NULL(2001, "IS NULL.");

    /**
     * 响应状态码
     */
    private final long code;

    /**
     * 状态描述消息
     */
    private final String message;

    /**
     * 构造函数
     *
     * @param code    状态码
     * @param message 描述消息
     */
    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取状态码
     *
     * @return 状态码
     */
    public long getCode() {
        return code;
    }

    /**
     * 获取描述消息
     *
     * @return 描述消息
     */
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("code = %d, message = %s.", code, message);
    }
}
