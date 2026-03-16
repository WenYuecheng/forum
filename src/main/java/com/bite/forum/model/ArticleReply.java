package com.bite.forum.model;

import lombok.Data;

import java.util.Date;

/**
 * 帖子回复实体类
 *
 * @author 温悦成
 * @since 2026-03-16
 */
@Data
public class ArticleReply {

    /**
     * 回复ID
     */
    private Long id;

    /**
     * 所属帖子ID
     */
    private Long articleId;

    /**
     * 发布回复的用户ID
     */
    private Long postUserId;

    /**
     * 被回复的回复ID（二级回复）
     */
    private Long replyId;

    /**
     * 被回复的用户ID
     */
    private Long replyUserId;

    /**
     * 回复内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 状态
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
     * 回复发布者信息
     */
    private User user;
}