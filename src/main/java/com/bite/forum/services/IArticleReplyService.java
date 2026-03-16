package com.bite.forum.services;

import com.bite.forum.model.ArticleReply;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 帖子回复服务接口
 *
 * @author 温悦成
 * @since 2026-03-16
 */
public interface IArticleReplyService {

    /**
     * 创建帖子回复
     * 涉及帖子回复数统计更新，开启事务
     *
     * @param articleReply 回帖实体对象
     */
    @Transactional(rollbackFor = Exception.class)
    void create(ArticleReply articleReply);

    /**
     * 根据帖子ID获取其名下的所有回复
     *
     * @param articleId 帖子ID
     * @return 回复列表
     */
    List<ArticleReply> selectByArticleId(Long articleId);
}
