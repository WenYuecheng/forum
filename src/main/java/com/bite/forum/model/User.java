package com.bite.forum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 用户实体类
 *
 * @author 温悦成
 * @since 2026-03-16
 */
@Schema(description = "用户信息")
@Data
public class User {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名（唯一凭证）
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 密码（序列化时忽略）
     */
    @JsonIgnore
    private String password;

    /**
     * 用户昵称
     */
    @Schema(description = "名称")
    private String nickname;

    /**
     * 手机号码
     */
    @Schema(description = "电话号")
    private String phoneNum;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 性别: 0-保密, 1-男, 2-女
     */
    private Byte gender;

    /**
     * MD5混淆盐值（序列化时忽略）
     */
    @JsonIgnore
    private String salt;

    /**
     * 头像URL
     */
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private String avatarUrl;

    /**
     * 发帖总数
     */
    private Integer articleCount;

    /**
     * 是否为管理员: 0-否, 1-是
     */
    private Byte isAdmin;

    /**
     * 个人简介/备注
     */
    private String remark;

    /**
     * 账号状态
     */
    private Byte state;

    /**
     * 逻辑删除标识（序列化时忽略）
     */
    @JsonIgnore
    private Byte deleteState;

    /**
     * 注册时间
     */
    private Date createTime;

    /**
     * 信息更新时间
     */
    private Date updateTime;
}