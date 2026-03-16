package com.bite.forum.controller;

import com.bite.forum.common.AppResult;
import com.bite.forum.common.ResultCode;
import com.bite.forum.config.AppConfig;
import com.bite.forum.model.Article;
import com.bite.forum.model.Board;
import com.bite.forum.model.User;
import com.bite.forum.services.IArticleService;
import com.bite.forum.services.IBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 帖子相关接口控制器
 * 提供帖子创建、列表查询、详情展示、点赞及删除功能
 *
 * @author 温悦成
 * @since 2026-03-16
 */
@RestController
@RequestMapping("/article")
@Tag(name = "ArticleController", description = "文章相关接口")
public class ArticleController {

    @Autowired
    private IArticleService articleService;

    @Autowired
    private IBoardService boardService;

    /**
     * 创建帖子
     *
     * @param request HTTP请求
     * @param boardId 板块ID
     * @param title   帖子标题
     * @param content 帖子详情内容
     * @return 统一响应结果
     */
    @Operation(summary = "创建文章")
    @PostMapping("/create")
    public AppResult<Void> create(HttpServletRequest request,
                                  @Parameter(description = "板块id") @NonNull Long boardId,
                                  @Parameter(description = "文章标题") @NonNull String title,
                                  @Parameter(description = "文章内容") @NonNull String content) {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        if (user.getState() == 1) {
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }
        Board board = boardService.selectById(boardId);
        if (board == null || board.getState() == 1 || board.getDeleteState() == 1) {
            return AppResult.failed(ResultCode.FAILED_CREATE);
        }

        Article article = new Article();
        article.setBoardId(boardId);
        article.setTitle(title);
        article.setContent(content);
        article.setUserId(user.getId());
        article.setLikeCount(0);
        articleService.create(article);
        return AppResult.success();
    }

    /**
     * 根据板块ID获取帖子列表
     *
     * @param boardId 可选板块ID，为空则获取首页全局帖子
     * @return 帖子列表
     */
    @Operation(summary = "根据板块id获取文章")
    @GetMapping("/getAllByBoardId")
    public AppResult<List<Article>> getAllByBoardId(@Parameter(description = "板块id")
                                                    @RequestParam(value = "boardId", required = false) Long boardId) {
        List<Article> result;
        if (boardId == null) {
            result = articleService.selectAll();
        } else {
            result = articleService.selectAllByBoardId(boardId);
        }

        if (result == null) {
            result = new ArrayList<>();
        }
        return AppResult.success(result);
    }

    /**
     * 获取帖子详细信息
     *
     * @param request HTTP请求
     * @param id      帖子ID
     * @return 帖子详情
     */
    @Operation(summary = "获取文章详情")
    @GetMapping("/details")
    public AppResult<Article> getDetails(HttpServletRequest request,
                                         @Parameter(description = "帖子id") @NonNull Long id) {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        Article article = articleService.selectDetailById(id);
        if (article == null) {
            return AppResult.failed(ResultCode.FAILED_NOT_EXISTS);
        }
        if (user.getId().equals(article.getUserId())) {
            article.setOwn(true);
        }
        return AppResult.success(article);
    }

    /**
     * 修改帖子内容
     *
     * @param request HTTP请求
     * @param id      帖子ID
     * @param title   新标题
     * @param content 新内容
     * @return 统一响应结果
     */
    @Operation(summary = "修改文章")
    @PostMapping("/modify")
    public AppResult<Void> modify(HttpServletRequest request,
                                  @Parameter(description = "帖子id") @NonNull Long id,
                                  @Parameter(description = "帖子标题") @NonNull String title,
                                  @Parameter(description = "帖子内容") @NonNull String content) {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        if (user.getState() == 1) {
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }
        Article article = articleService.selectById(id);
        if (article == null) {
            return AppResult.failed(ResultCode.FAILED_NOT_EXISTS);
        }
        if (!user.getId().equals(article.getUserId())) {
            return AppResult.failed(ResultCode.FAILED_UNAUTHORIZED);
        }
        if (article.getState() == 1) {
            return AppResult.failed(ResultCode.FAILED_FORBIDDEN);
        }
        articleService.modify(id, title, content);
        return AppResult.success();
    }

    /**
     * 为帖子点赞
     *
     * @param request HTTP请求
     * @param id      帖子ID
     * @return 统一响应结果
     */
    @Operation(summary = "点赞")
    @PostMapping("/thumbsUp")
    public AppResult<Void> thumbsUp(HttpServletRequest request,
                                    @Parameter(description = "帖子id") @NonNull Long id) {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        if (user.getState() == 1) {
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }
        articleService.thumbsUpById(id);
        return AppResult.success();
    }

    /**
     * 删除帖子
     *
     * @param request HTTP请求
     * @param id      帖子ID
     * @return 统一响应结果
     */
    @Operation(summary = "删除帖子")
    @PostMapping("/delete")
    public AppResult<Void> deleteById(HttpServletRequest request,
                                      @Parameter(description = "帖子id") @NonNull Long id) {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        if (user.getState() == 1) {
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }
        Article article = articleService.selectById(id);
        if (article == null) {
            return AppResult.failed(ResultCode.FAILED_NOT_EXISTS);
        }
        if (!user.getId().equals(article.getUserId())) {
            return AppResult.failed(ResultCode.FAILED_UNAUTHORIZED);
        }
        if (article.getState() == 1) {
            return AppResult.failed(ResultCode.FAILED_FORBIDDEN);
        }
        articleService.deleteById(id);
        return AppResult.success();
    }

    /**
     * 获取指定用户的帖子列表
     *
     * @param request HTTP请求
     * @param userId  用户ID，为空则获取当前登录用户的列表
     * @return 帖子列表
     */
    @Operation(summary = "获取用户的帖子列表")
    @GetMapping("/getAllByUserId")
    public AppResult<List<Article>> getAllByUserId(HttpServletRequest request,
                                                   @Parameter(description = "用户id")
                                                   @RequestParam(value = "userId", required = false) Long userId) {
        if (userId == null) {
            HttpSession session = request.getSession(false);
            User user = (User) session.getAttribute(AppConfig.USER_SESSION);
            userId = user.getId();
        }
        List<Article> articles = articleService.selectByUserId(userId);
        return AppResult.success(articles);
    }
}
