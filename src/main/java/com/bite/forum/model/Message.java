package com.bite.forum.model;

import lombok.Data;

import java.util.Date;

/**
 * 站内消息实体类
 *
 * @author 温悦成
 * @since 2026-03-16
 */
@Data
public class Message {

    /**
     * 消息ID
     */
    private Long id;

    /**
     * 发送者用户ID
     */
    private Long postUserId;

    /**
     * 接收者用户ID
     */
    private Long receiveUserId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 状态: 0-未读, 1-已读
     */
    private Byte state;

    /**
     * 逻辑删除标识: 0-未删除, 1-已删除
     */
    private Byte deleteState;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 关联的发送者用户信息
     */
    private User postUser;
}