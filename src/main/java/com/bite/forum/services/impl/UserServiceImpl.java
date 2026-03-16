package com.bite.forum.services.impl;

import com.bite.forum.common.AppResult;
import com.bite.forum.common.ResultCode;
import com.bite.forum.dao.UserMapper;
import com.bite.forum.exception.ApplicationException;
import com.bite.forum.model.User;
import com.bite.forum.services.IUserService;
import com.bite.forum.utils.MD5Utils;
import com.bite.forum.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 用户服务实现类
 * 处理用户注册、登录、信息修改及发帖计数统计
 *
 * @author 温悦成
 * @since 2026-03-16
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void createNormalUser(User user) {
        if (user == null) {
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }

        // 校验用户是否已存在
        User existUser = userMapper.selectByUserName(user.getUsername());
        if (existUser != null) {
            log.info("用户已存在: username = {}", user.getUsername());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_EXISTS));
        }

        // 设置默认值
        if (user.getGender() == null) {
            // 默认性别设为女性（原逻辑为2）
            user.setGender((byte) 2);
        }
        user.setIsAdmin((byte) 0);
        user.setAvatarUrl(null);
        user.setArticleCount(0);
        user.setState((byte) 0);
        user.setDeleteState((byte) 0);

        Date now = new Date();
        user.setCreateTime(now);
        user.setUpdateTime(now);

        int row = userMapper.insertSelective(user);
        if (row != 1) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
    }

    @Override
    public User selectByUserName(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }
        return userMapper.selectByUserName(username);
    }

    @Override
    public User login(String username, String password) {
        if (StringUtils.isAnyEmpty(username, password)) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NOT_EXISTS));
        }
        User user = userMapper.selectByUserName(username);
        if (user == null) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }
        if (!MD5Utils.verifyOriginalAndCiphertext(password, user.getSalt(), user.getPassword())) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_LOGIN));
        }
        return user;
    }

    @Override
    public User selectById(Long id) {
        if (id == null) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addOneArticleCountById(Long id) {
        if (id == null || id <= 0) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setArticleCount(user.getArticleCount() + 1);
        int row = userMapper.updateByPrimaryKeySelective(updateUser);
        if (row != 1) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
    }

    @Override
    public void subOneArticleCountById(Long id) {
        if (id == null || id <= 0) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setArticleCount(user.getArticleCount() - 1);
        int row = userMapper.updateByPrimaryKeySelective(updateUser);
        if (row != 1) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
    }

    @Override
    public void modifyInfo(User user) {
        if (user == null || user.getId() == null) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        User existUser = userMapper.selectByPrimaryKey(user.getId());
        if (existUser == null) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }

        boolean hasChanged = false;
        User updateUser = new User();
        updateUser.setId(user.getId());

        // 用户名修改校验
        if (StringUtils.isNotEmpty(user.getUsername()) && !user.getUsername().equals(existUser.getUsername())) {
            User checkUser = userMapper.selectByUserName(user.getUsername());
            if (checkUser != null) {
                throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_EXISTS));
            }
            updateUser.setUsername(user.getUsername());
            hasChanged = true;
        }

        // 其它字段修改判断
        if (StringUtils.isNotEmpty(user.getNickname()) && !user.getNickname().equals(existUser.getNickname())) {
            updateUser.setNickname(user.getNickname());
            hasChanged = true;
        }
        if (StringUtils.isNotEmpty(user.getPassword()) && !user.getPassword().equals(existUser.getPassword())) {
            updateUser.setPassword(user.getPassword());
            hasChanged = true;
        }
        if (StringUtils.isNotEmpty(user.getEmail()) && !user.getEmail().equals(existUser.getEmail())) {
            updateUser.setEmail(user.getEmail());
            hasChanged = true;
        }
        if (StringUtils.isNotEmpty(user.getPhoneNum()) && !user.getPhoneNum().equals(existUser.getPhoneNum())) {
            updateUser.setPhoneNum(user.getPhoneNum());
            hasChanged = true;
        }
        if (StringUtils.isNotEmpty(user.getRemark()) && !user.getRemark().equals(existUser.getRemark())) {
            updateUser.setRemark(user.getRemark());
            hasChanged = true;
        }
        if (user.getGender() != null && !user.getGender().equals(existUser.getGender())) {
            updateUser.setGender(user.getGender());
            hasChanged = true;
        }

        if (!hasChanged) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        int row = userMapper.updateByPrimaryKeySelective(updateUser);
        if (row != 1) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
    }

    @Override
    public void modifyPassword(Long id, String newPassword, String oldPassword) {
        if (id == null || id <= 0 || StringUtils.isAnyEmpty(newPassword, oldPassword)) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null || user.getDeleteState() == 1) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }

        String oldEncryptPassword = MD5Utils.md5Salt(oldPassword, user.getSalt());
        if (!oldEncryptPassword.equalsIgnoreCase(user.getPassword())) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_LOGIN));
        }

        String newSalt = UUIDUtils.UUID_32();
        String newEncryptPassword = MD5Utils.md5Salt(newPassword, newSalt);

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setUpdateTime(new Date());
        updateUser.setSalt(newSalt);
        updateUser.setPassword(newEncryptPassword);

        int row = userMapper.updateByPrimaryKeySelective(updateUser);
        if (row != 1) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
    }
}
