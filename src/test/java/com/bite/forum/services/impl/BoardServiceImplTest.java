package com.bite.forum.services.impl;

import com.bite.forum.model.Board;
import com.bite.forum.services.IBoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class BoardServiceImplTest {
    @Autowired
    private IBoardService boardService;

    @Test
    void selectByNum() {
        List<Board> boards = boardService.selectByNum(5);
        System.out.println(boards);
    }

    @Test
    @Transactional
    void addOneArticleCountById() {
        boardService.addOneArticleCountById(1l);
    }

    @Test
    void subOneArticleCountById() {
        boardService.subOneArticleCountById(1l);
    }
}