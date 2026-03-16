package com.bite.forum.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 帖子实体类
 *
 * @author 温悦成
 * @since 2026-03-16
 */
@Data
public class Article {

    /**
     * 帖子ID
     */
    private Long id;

    /**
     * 所属板块ID
     */
    private Long boardId;

    /**
     * 作者用户ID
     */
    private Long userId;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 访问量/阅读量
     */
    private Integer visitCount;

    /**
     * 回复数/评论数
     */
    private Integer replyCount;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 状态: 0-公开, 1-私密, 等
     */
    private Byte state;

    /**
     * 逻辑删除标识: 0-正常, 1-已删除
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
     * 帖子正文内容
     */
    private String content;

    /**
     * 关联的作者信息
     */
    private User user;

    /**
     * 关联的板块信息
     */
    private Board board;

    /**
     * 当前查看者是否为该帖子的作者
     */
    @Schema(description = "用户是不是作者")
    private Boolean own;
}