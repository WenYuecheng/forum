package com.bite.forum.controller;

import com.bite.forum.common.AppResult;
import com.bite.forum.common.ResultCode;
import com.bite.forum.config.AppConfig;
import com.bite.forum.model.Article;
import com.bite.forum.model.ArticleReply;
import com.bite.forum.model.User;
import com.bite.forum.services.IArticleReplyService;
import com.bite.forum.services.IArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 帖子回复控制器
 * 处理对帖子的评论及回复逻辑
 *
 * @author 温悦成
 * @since 2026-03-16
 */
@RestController
@Tag(name = "ArticleReplyController", description = "文章回复")
@RequestMapping("/reply")
public class ArticleReplyController {

    @Autowired
    private IArticleReplyService replyService;

    @Autowired
    private IArticleService articleService;

    /**
     * 创建帖子回复
     *
     * @param request   HTTP请求
     * @param articleId 帖子ID
     * @param content   回复内容
     * @return 统一响应结果
     */
    @Operation(summary = "创建文章回复")
    @PostMapping("/create")
    public AppResult<Void> create(HttpServletRequest request,
                                  @Parameter(description = "帖子id") @NonNull Long articleId,
                                  @Parameter(description = "帖子内容") @NonNull String content) {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        if (user.getState() == 1) {
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }
        Article article = articleService.selectById(articleId);
        if (article == null || article.getDeleteState() == 1) {
            return AppResult.failed(ResultCode.FAILED_NOT_EXISTS);
        }
        if (article.getState() == 1) {
            return AppResult.failed(ResultCode.FAILED_FORBIDDEN);
        }

        ArticleReply articleReply = new ArticleReply();
        articleReply.setArticleId(articleId);
        articleReply.setContent(content);
        articleReply.setPostUserId(user.getId());
        replyService.create(articleReply);
        return AppResult.success();
    }

    /**
     * 根据帖子ID获取所有回复明细
     *
     * @param articleId 帖子ID
     * @return 帖子回复列表
     */
    @Operation(summary = "根据帖子id查询所有回复")
    @GetMapping("/getReplies")
    public AppResult<List<ArticleReply>> getRepliesByArticleId(@Parameter(description = "帖子id") @NonNull Long articleId) {
        Article article = articleService.selectById(articleId);
        if (article == null || article.getDeleteState() == 1) {
            return AppResult.failed(ResultCode.FAILED_NOT_EXISTS);
        }
        List<ArticleReply> articleReplyList = replyService.selectByArticleId(articleId);
        return AppResult.success(articleReplyList);
    }
}
