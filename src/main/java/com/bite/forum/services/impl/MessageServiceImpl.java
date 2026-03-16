package com.bite.forum.services.impl;

import com.bite.forum.common.AppResult;
import com.bite.forum.common.ResultCode;
import com.bite.forum.dao.MessageMapper;
import com.bite.forum.exception.ApplicationException;
import com.bite.forum.model.Message;
import com.bite.forum.model.User;
import com.bite.forum.services.IMessageService;
import com.bite.forum.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 站内信消息服务实现类
 *
 * @author 温悦成
 * @since 2026-03-16
 */
@Service
public class MessageServiceImpl implements IMessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private IUserService userService;

    @Override
    public void create(Message message) {
        if (message == null || message.getPostUserId() == null || message.getReceiveUserId() == null) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }

        // 校验接收者用户合法性
        User user = userService.selectById(message.getReceiveUserId());
        if (user == null || user.getState() == 1) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }

        // 初始化消息状态
        message.setState((byte) 0);
        message.setDeleteState((byte) 0);

        Date now = new Date();
        message.setCreateTime(now);
        message.setUpdateTime(now);

        int row = messageMapper.insertSelective(message);
        if (row != 1) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
    }

    @Override
    public Integer selectUnreadCount(Long userId) {
        if (userId == null) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }
        return messageMapper.selectUnreadCount(userId);
    }

    @Override
    public List<Message> selectByReceiveUserId(Long receiveUserId) {
        if (receiveUserId == null) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }
        return messageMapper.selectByReceiveUserId(receiveUserId);
    }

    @Override
    public void updateStateById(Long id, Byte state) {
        if (id == null || id <= 0 || state < 0 || state > 2) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        Message updateMessage = new Message();
        updateMessage.setId(id);
        updateMessage.setState(state);
        updateMessage.setUpdateTime(new Date());

        int row = messageMapper.updateByPrimaryKeySelective(updateMessage);
        if (row != 1) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
    }

    @Override
    public Message selectById(Long id) {
        if (id == null || id <= 0) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        return messageMapper.selectByPrimaryKey(id);
    }

    @Override
    public void reply(Long repliedId, Message message) {
        if (repliedId == null || repliedId <= 0) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        // 验证原消息是否存在且未被删除
        Message existsMessage = messageMapper.selectByPrimaryKey(repliedId);
        if (existsMessage == null || existsMessage.getDeleteState() == 1) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NOT_EXISTS));
        }

        // 1. 将原消息标记为“已回复”
        updateStateById(repliedId, (byte) 2);

        // 2. 创建并发送回复消息
        create(message);
    }
}
