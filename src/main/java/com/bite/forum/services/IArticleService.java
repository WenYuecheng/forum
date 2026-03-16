package com.bite.forum.services;

import com.bite.forum.model.Article;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 帖子服务接口
 *
 * @author 温悦成
 * @since 2026-03-16
 */
public interface IArticleService {

    /**
     * 创建帖子
     * 涉及板块和用户发帖数统计更新，开启事务
     *
     * @param article 帖子实体对象
     */
    @Transactional(rollbackFor = Exception.class)
    void create(Article article);

    /**
     * 查询所有公开帖子
     *
     * @return 帖子列表
     */
    List<Article> selectAll();

    /**
     * 根据板块ID查询帖子列表
     *
     * @param boardId 板块ID
     * @return 帖子列表
     */
    List<Article> selectAllByBoardId(Long boardId);

    /**
     * 获取帖子详细内容，并增加访问计数
     *
     * @param id 帖子ID
     * @return 帖子详情对象
     */
    Article selectDetailById(Long id);

    /**
     * 根据主键查询帖子基本信息
     *
     * @param id 帖子ID
     * @return 帖子实体对象
     */
    Article selectById(Long id);

    /**
     * 修改帖子标题和正文
     *
     * @param id      帖子ID
     * @param title   新标题
     * @param content 新内容
     */
    void modify(Long id, String title, String content);

    /**
     * 为帖子点赞增加计数
     *
     * @param id 帖子ID
     */
    void thumbsUpById(Long id);

    /**
     * 逻辑删除帖子
     * 涉及对该板块和作者的发帖数扣减，开启事务
     *
     * @param id 帖子ID
     */
    @Transactional(rollbackFor = Exception.class)
    void deleteById(Long id);

    /**
     * 增加文章回复计数
     *
     * @param id 帖子ID
     */
    void addOneReplyCountById(Long id);

    /**
     * 根据作者ID查询其发布的所有帖子列表
     *
     * @param userId 用户ID
     * @return 帖子列表
     */
    List<Article> selectByUserId(Long userId);
}
