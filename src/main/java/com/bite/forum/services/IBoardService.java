package com.bite.forum.services;

import com.bite.forum.model.Board;

import java.util.List;

/**
 * 板块服务接口
 *
 * @author 温悦成
 * @since 2026-03-16
 */
public interface IBoardService {

    /**
     * 获取指定数量的展示板块列表
     *
     * @param num 获取数量
     * @return 板块列表
     */
    List<Board> selectByNum(Integer num);

    /**
     * 为板块增加一个帖子计数
     *
     * @param id 板块ID
     */
    void addOneArticleCountById(Long id);

    /**
     * 根据ID查询板块详情
     *
     * @param id 板块ID
     * @return 板块详情对象
     */
    Board selectById(Long id);

    /**
     * 为板块减少一个帖子计数
     *
     * @param id 板块ID
     */
    void subOneArticleCountById(Long id);
}
