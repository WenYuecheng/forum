package com.bite.forum.services;

import com.bite.forum.model.Message;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 站内信消息服务接口
 *
 * @author 温悦成
 * @since 2026-03-16
 */
public interface IMessageService {

    /**
     * 发送站内信
     *
     * @param message 消息实体对象
     */
    void create(Message message);

    /**
     * 根据用户ID查询未读消息总数
     *
     * @param userId 用户ID
     * @return 未读数
     */
    Integer selectUnreadCount(Long userId);

    /**
     * 根据接收者ID获取其收到的所有消息列表
     *
     * @param receiveUserId 接收者ID
     * @return 消息列表
     */
    List<Message> selectByReceiveUserId(Long receiveUserId);

    /**
     * 更新消息状态
     *
     * @param id    消息ID
     * @param state 目标状态: 0-未读, 1-已读, 2-已回复
     */
    void updateStateById(Long id, Byte state);

    /**
     * 根据主键查询消息详情
     *
     * @param id 消息ID
     * @return 消息实体对象
     */
    Message selectById(Long id);

    /**
     * 回复站内信
     * 自动更新原消息状态为已回复，并创建新消息，开启事务
     *
     * @param repliedId 被回复的消息ID
     * @param message   新回复的消息实体
     */
    @Transactional(rollbackFor = Exception.class)
    void reply(Long repliedId, Message message);
}
