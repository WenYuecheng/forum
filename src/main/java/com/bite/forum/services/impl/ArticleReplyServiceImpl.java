package com.bite.forum.services.impl;

import com.bite.forum.common.AppResult;
import com.bite.forum.common.ResultCode;
import com.bite.forum.dao.ArticleReplyMapper;
import com.bite.forum.exception.ApplicationException;
import com.bite.forum.model.ArticleReply;
import com.bite.forum.services.IArticleReplyService;
import com.bite.forum.services.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 帖子回复服务接口实现类
 *
 * @author 温悦成
 * @since 2026-03-16
 */
@Service
public class ArticleReplyServiceImpl implements IArticleReplyService {

    @Autowired
    private ArticleReplyMapper replyMapper;

    @Autowired
    private IArticleService articleService;

    @Override
    public void create(ArticleReply articleReply) {
        if (articleReply == null || articleReply.getArticleId() == null || articleReply.getArticleId() <= 0
                || articleReply.getPostUserId() == null || articleReply.getContent() == null) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NOT_EXISTS));
        }

        // 初始化回复基础属性
        articleReply.setReplyId(null);
        articleReply.setReplyUserId(null);
        articleReply.setLikeCount(0);
        articleReply.setState((byte) 0);
        articleReply.setDeleteState((byte) 0);

        Date now = new Date();
        articleReply.setCreateTime(now);
        articleReply.setUpdateTime(now);

        int row = replyMapper.insertSelective(articleReply);
        if (row != 1) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }

        // 同步增加帖子的回复计数
        articleService.addOneReplyCountById(articleReply.getArticleId());
    }

    @Override
    public List<ArticleReply> selectByArticleId(Long articleId) {
        if (articleId == null || articleId <= 0) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NOT_EXISTS));
        }
        return replyMapper.selectByArticleId(articleId);
    }
}
