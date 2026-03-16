package com.bite.forum.services.impl;

import com.bite.forum.model.ArticleReply;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ArticleReplyServiceImplTest {

    @Autowired
    private ArticleReplyServiceImpl articleReplyService;
    @Test
    void create() {
        ArticleReply  articleReply = new ArticleReply();
        articleReply.setArticleId(2l);
        articleReply.setPostUserId(1l);
        articleReply.setContent("测试回复");
        articleReplyService.create(articleReply);
    }

    @Test
    void selectByArticleId() {
        List<ArticleReply> articleReplies = articleReplyService.selectByArticleId(1l);
        System.out.println(articleReplies);
    }
}