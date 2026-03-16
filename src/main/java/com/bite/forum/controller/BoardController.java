package com.bite.forum.controller;

import com.bite.forum.common.AppResult;
import com.bite.forum.common.ResultCode;
import com.bite.forum.exception.ApplicationException;
import com.bite.forum.model.Board;
import com.bite.forum.services.IBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 版块接口控制器
 * 提供版块列表查询及详情获取功能
 *
 * @author 温悦成
 * @since 2026-03-16
 */
@RestController
@Tag(name = "BoardController", description = "板块接口")
@RequestMapping("/board")
public class BoardController {

    @Value("${bit-forum.index.board-num:9}")
    private Integer indexBoardNum;

    @Autowired
    private IBoardService boardService;

    /**
     * 获取首页展示的板块列表
     *
     * @return 包含板块列表的统一响应结果
     */
    @Operation(summary = "获取首页板块列表")
    @GetMapping("/topList")
    public AppResult<List<Board>> topList() {
        List<Board> boards = boardService.selectByNum(indexBoardNum);
        if (boards == null) {
            boards = new ArrayList<>();
        }
        return AppResult.success(boards);
    }

    /**
     * 根据板块ID获取板块详情
     *
     * @param id 板块ID
     * @return 包含板块详情的统一响应结果
     * @throws ApplicationException 当板块不存在或已被删除时抛出
     */
    @Operation(summary = "根据id获取板块")
    @GetMapping("/getById")
    public AppResult<Board> getById(@Parameter(description = "板块id") @NonNull Long id) {
        Board board = boardService.selectById(id);
        if (board == null || board.getDeleteState() == 1) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NOT_EXISTS));
        }
        return AppResult.success(board);
    }
}
