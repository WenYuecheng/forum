package com.bite.forum.model;

import lombok.Data;

import java.util.Date;

/**
 * 板块实体类
 *
 * @author 温悦成
 * @since 2026-03-16
 */
@Data
public class Board {

    /**
     * 板块ID
     */
    private Long id;

    /**
     * 板块名称
     */
    private String name;

    /**
     * 板块下帖子总数
     */
    private Integer articleCount;

    /**
     * 排序权重
     */
    private Integer sort;

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
}