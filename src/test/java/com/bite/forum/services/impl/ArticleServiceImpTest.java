package com.bite.forum.services.impl;

import com.bite.forum.model.Article;
import com.bite.forum.services.IArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ArticleServiceImplTest {
    @Autowired
    private IArticleService articleService;

    @Test
    void create() {
        Article article = new Article();
        article.setUserId(1l);
        article.setBoardId(1l);
        article.setTitle("测试文章");
        article.setContent("测试内容");
        articleService.create(article);

    }

    @Test
    void selectAll() {
        List<Article> articles = articleService.selectAll();
        System.out.println(articles);
    }

    @Test
    void selectByBoardId() {
        List<Article> articles = articleService.selectAllByBoardId(1l);
        System.out.println(articles);
    }

    @Test
    void selectDetailById() {
        Article article = articleService.selectDetailById(1l);
        System.out.println(article);
    }

    @Test
    void modify() {
        articleService.modify(2l,"修改标题","修改内容");
    }

    @Test
    void thumbsUpById() {
        articleService.thumbsUpById(1l);

    }

    @Test
    void deleteById() {
        articleService.deleteById(4l);
    }

    @Test
    void addOneReplyCountById() {
        articleService.addOneReplyCountById(1l);
    }

    @Test
    void selectByUserId() {
        List<Article> articles = articleService.selectByUserId(1l);
        System.out.println(articles);
    }
}