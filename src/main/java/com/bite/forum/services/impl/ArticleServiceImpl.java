package com.bite.forum.services.impl;

import com.bite.forum.common.AppResult;
import com.bite.forum.common.ResultCode;
import com.bite.forum.dao.ArticleMapper;
import com.bite.forum.exception.ApplicationException;
import com.bite.forum.model.Article;
import com.bite.forum.model.Board;
import com.bite.forum.model.User;
import com.bite.forum.services.IArticleService;
import com.bite.forum.services.IBoardService;
import com.bite.forum.services.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 帖子业务接口实现类
 *
 * @author 温悦成
 * @since 2026-03-16
 */
@Service
public class ArticleServiceImpl implements IArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private IUserService userService;

    @Autowired
    private IBoardService boardService;

    @Override
    public void create(Article article) {
        if (article == null || StringUtils.isAnyEmpty(article.getTitle(), article.getContent())) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NOT_EXISTS));
        }

        // 初始化基础计数值
        article.setVisitCount(0);
        article.setReplyCount(0);
        article.setLikeCount(0);
        article.setDeleteState((byte) 0);
        article.setState((byte) 0);

        Date now = new Date();
        article.setCreateTime(now);
        article.setUpdateTime(now);

        int row = articleMapper.insertSelective(article);
        if (row <= 0) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }

        // 同步增加用户发帖数
        User user = userService.selectById(article.getUserId());
        if (user == null) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }
        userService.addOneArticleCountById(user.getId());

        // 同步增加板块帖子数
        Board board = boardService.selectById(article.getBoardId());
        if (board == null) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NOT_EXISTS));
        }
        boardService.addOneArticleCountById(board.getId());
    }

    @Override
    public List<Article> selectAll() {
        return articleMapper.selectAll();
    }

    @Override
    public List<Article> selectAllByBoardId(Long boardId) {
        if (boardId == null || boardId <= 0) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NOT_EXISTS));
        }
        Board board = boardService.selectById(boardId);
        if (board == null) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NOT_EXISTS));
        }
        return articleMapper.selectByBoardId(boardId);
    }

    @Override
    public Article selectDetailById(Long id) {
        if (id == null || id <= 0) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NOT_EXISTS));
        }
        Article article = articleMapper.selectDetailById(id);
        if (article == null) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NOT_EXISTS));
        }

        // 访问量增加逻辑
        Article updateArticle = new Article();
        updateArticle.setId(article.getId());
        updateArticle.setVisitCount(article.getVisitCount() + 1);
        int row = articleMapper.updateByPrimaryKeySelective(updateArticle);
        if (row != 1) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }

        article.setVisitCount(article.getVisitCount() + 1);
        return article;
    }

    @Override
    public Article selectById(Long id) {
        if (id == null || id <= 0) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NOT_EXISTS));
        }
        return articleMapper.selectByPrimaryKey(id);
    }

    @Override
    public void modify(Long id, String title, String content) {
        if (id == null || id <= 0 || StringUtils.isAnyEmpty(title, content)) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NOT_EXISTS));
        }
        Article updateArticle = new Article();
        updateArticle.setId(id);
        updateArticle.setTitle(title);
        updateArticle.setContent(content);
        updateArticle.setUpdateTime(new Date());

        int row = articleMapper.updateByPrimaryKeySelective(updateArticle);
        if (row != 1) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
    }

    @Override
    public void thumbsUpById(Long id) {
        if (id == null || id <= 0) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NOT_EXISTS));
        }
        Article article = articleMapper.selectByPrimaryKey(id);
        if (article == null || article.getState() == 1 || article.getDeleteState() == 1) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NOT_EXISTS));
        }

        Article updateArticle = new Article();
        updateArticle.setId(id);
        updateArticle.setLikeCount(article.getLikeCount() + 1);
        updateArticle.setUpdateTime(new Date());

        int row = articleMapper.updateByPrimaryKeySelective(updateArticle);
        if (row != 1) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
    }

    @Override
    public void deleteById(Long id) {
        if (id == null || id <= 0) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NOT_EXISTS));
        }
        Article article = articleMapper.selectByPrimaryKey(id);
        if (article == null || article.getState() == 1 || article.getDeleteState() == 1) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NOT_EXISTS));
        }

        // 执行逻辑删除
        Article updateArticle = new Article();
        updateArticle.setId(id);
        updateArticle.setDeleteState((byte) 1);
        updateArticle.setUpdateTime(new Date());

        int row = articleMapper.updateByPrimaryKeySelective(updateArticle);
        if (row != 1) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }

        // 同步减少发帖计数
        boardService.subOneArticleCountById(article.getBoardId());
        userService.subOneArticleCountById(article.getUserId());
    }

    @Override
    public void addOneReplyCountById(Long id) {
        if (id == null || id <= 0) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NOT_EXISTS));
        }
        Article article = articleMapper.selectByPrimaryKey(id);
        if (article == null) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NOT_EXISTS));
        }

        Article updateArticle = new Article();
        updateArticle.setId(article.getId());
        updateArticle.setReplyCount(article.getReplyCount() + 1);
        updateArticle.setUpdateTime(new Date());

        int row = articleMapper.updateByPrimaryKeySelective(updateArticle);
        if (row != 1) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
    }

    @Override
    public List<Article> selectByUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NOT_EXISTS));
        }
        User user = userService.selectById(userId);
        if (user == null || user.getState() == 1) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NOT_EXISTS));
        }
        return articleMapper.selectByUserId(userId);
    }
}
