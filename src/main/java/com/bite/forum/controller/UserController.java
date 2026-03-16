package com.bite.forum.controller;

import com.bite.forum.common.AppResult;
import com.bite.forum.common.ResultCode;
import com.bite.forum.config.AppConfig;
import com.bite.forum.model.User;
import com.bite.forum.services.IUserService;
import com.bite.forum.utils.MD5Utils;
import com.bite.forum.utils.UUIDUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户接口控制器
 * 处理用户注册、登录、信息查询及修改等业务
 *
 * @author 温悦成
 * @since 2026-03-16
 */
@Tag(name = "UserController", description = "用户接口")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 用户注册
     *
     * @param username       用户名
     * @param nickname       昵称
     * @param password       密码
     * @param passwordRepeat 重复密码
     * @return 统一响应结果
     */
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public AppResult<Void> register(@Parameter(description = "用户名") @NonNull String username,
                                    @Parameter(description = "名称") @NonNull String nickname,
                                    @Parameter(description = "密码") @NonNull String password,
                                    @Parameter(description = "重复密码") @NonNull String passwordRepeat) {
        if (!password.equals(passwordRepeat)) {
            return AppResult.failed(ResultCode.FAILED_TWO_PWD_NOT_SAME);
        }
        User user = new User();
        user.setUsername(username);
        user.setNickname(nickname);
        String salt = UUIDUtils.UUID_32();
        String encryptPassword = MD5Utils.md5Salt(password, salt);
        user.setPassword(encryptPassword);
        user.setSalt(salt);
        userService.createNormalUser(user);
        return AppResult.success();
    }

    /**
     * 用户登录
     *
     * @param request  HTTP请求
     * @param username 用户名
     * @param password 密码
     * @return 统一响应结果
     */
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public AppResult<Void> login(HttpServletRequest request,
                                 @Parameter(description = "用户名") @NonNull String username,
                                 @Parameter(description = "密码") @NonNull String password) {
        User user = userService.login(username, password);
        if (user == null) {
            return AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS);
        }
        HttpSession session = request.getSession(true);
        session.setAttribute(AppConfig.USER_SESSION, user);
        return AppResult.success();
    }

    /**
     * 获取用户信息
     *
     * @param request HTTP请求
     * @param id      用户ID（可选，为空则获取当前登录用户）
     * @return 用户信息响应
     */
    @Operation(summary = "获取用户信息")
    @GetMapping("/info")
    public AppResult<User> getUserInfo(HttpServletRequest request,
                                       @Parameter(description = "用户id", required = false)
                                       @RequestParam(value = "id", required = false) Long id) {
        User user;
        if (id == null) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute(AppConfig.USER_SESSION) == null) {
                return AppResult.failed(ResultCode.FAILED_UNAUTHORIZED);
            }
            user = (User) session.getAttribute(AppConfig.USER_SESSION);
        } else {
            user = userService.selectById(id);
        }
        if (user == null) {
            return AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS);
        }
        return AppResult.success(user);
    }

    /**
     * 退出登录
     *
     * @param request HTTP请求
     * @return 成功提示
     */
    @Operation(summary = "退出登录")
    @GetMapping("/logout")
    public AppResult<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
        }
        return AppResult.success("退出成功");
    }

    /**
     * 修改用户信息
     *
     * @param username 用户名
     * @param nickname 昵称
     * @param gender   性别
     * @param email    邮箱
     * @param phoneNum 电话
     * @param remark   个人简介
     * @param request  HTTP请求
     * @return 修改后的用户信息
     */
    @Operation(summary = "修改用户信息")
    @PostMapping("/modifyInfo")
    public AppResult<User> modifyInfo(@Parameter(description = "用户名") @RequestParam(value = "username", required = false) String username,
                                      @Parameter(description = "名称") @RequestParam(value = "nickname", required = false) String nickname,
                                      @Parameter(description = "性别") @RequestParam(value = "gender", required = false) Byte gender,
                                      @Parameter(description = "邮箱") @RequestParam(value = "email", required = false) String email,
                                      @Parameter(description = "电话") @RequestParam(value = "phoneNum", required = false) String phoneNum,
                                      @Parameter(description = "简介") @RequestParam(value = "remark", required = false) String remark,
                                      HttpServletRequest request) {
        if (StringUtils.isAllEmpty(username, nickname, email, phoneNum, remark)) {
            return AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE);
        }
        HttpSession session = request.getSession(false);
        User currentSessionUser = (User) session.getAttribute(AppConfig.USER_SESSION);
        User updateUser = new User();
        updateUser.setId(currentSessionUser.getId());
        updateUser.setUsername(username);
        updateUser.setNickname(nickname);
        updateUser.setGender(gender);
        updateUser.setEmail(email);
        updateUser.setPhoneNum(phoneNum);
        updateUser.setRemark(remark);
        userService.modifyInfo(updateUser);
        User newUser = userService.selectById(currentSessionUser.getId());
        session.setAttribute(AppConfig.USER_SESSION, newUser);
        return AppResult.success(newUser);
    }

    /**
     * 修改用户密码
     *
     * @param newPassword    新密码
     * @param passwordRepeat 确认新密码
     * @param oldPassword    旧密码
     * @param request        HTTP请求
     * @return 成功响应
     */
    @Operation(summary = "修改密码")
    @PostMapping("/modifyPwd")
    public AppResult<Void> modifyPassword(@Parameter(description = "新密码") @RequestParam(value = "newPassword") @NonNull String newPassword,
                                          @Parameter(description = "确认密码") @RequestParam(value = "passwordRepeat") @NonNull String passwordRepeat,
                                          @Parameter(description = "旧密码") @RequestParam(value = "oldPassword") @NonNull String oldPassword,
                                          HttpServletRequest request) {
        if (!newPassword.equals(passwordRepeat)) {
            return AppResult.failed(ResultCode.FAILED_TWO_PWD_NOT_SAME);
        }
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        userService.modifyPassword(user.getId(), newPassword, oldPassword);
        return AppResult.success();
    }
}
