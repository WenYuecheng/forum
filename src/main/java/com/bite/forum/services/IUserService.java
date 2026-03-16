package com.bite.forum.services;

import com.bite.forum.model.User;

/**
 * 用户服务接口
 * 定义用户相关的业务逻辑操作
 *
 * @author 温悦成
 * @since 2026-03-16
 */
public interface IUserService {

    /**
     * 创建普通用户
     *
     * @param user 用户信息对象
     */
    void createNormalUser(User user);

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户详情对象
     */
    User selectByUserName(String username);

    /**
     * 用户登录校验
     *
     * @param username 用户名
     * @param password 密码（明文）
     * @return 登录成功的用户信息
     */
    User login(String username, String password);

    /**
     * 根据ID查询用户信息
     *
     * @param id 用户ID
     * @return 用户详情对象
     */
    User selectById(Long id);

    /**
     * 为用户增加一个发帖计数
     *
     * @param id 用户ID
     */
    void addOneArticleCountById(Long id);

    /**
     * 为用户减少一个发帖计数
     *
     * @param id 用户ID
     */
    void subOneArticleCountById(Long id);

    /**
     * 修改用户基本资料
     *
     * @param user 包含新资料的用户对象
     */
    void modifyInfo(User user);

    /**
     * 修改个人登录密码
     *
     * @param id          用户ID
     * @param newPassword 新密码（明文）
     * @param oldPassword 旧密码（明文）
     */
    void modifyPassword(Long id, String newPassword, String oldPassword);
}
