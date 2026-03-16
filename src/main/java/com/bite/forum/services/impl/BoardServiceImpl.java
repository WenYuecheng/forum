package com.bite.forum.services.impl;

import com.bite.forum.common.AppResult;
import com.bite.forum.common.ResultCode;
import com.bite.forum.dao.BoardMapper;
import com.bite.forum.exception.ApplicationException;
import com.bite.forum.model.Board;
import com.bite.forum.services.IBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 板块服务接口实现类
 *
 * @author 温悦成
 * @since 2026-03-16
 */
@Service
public class BoardServiceImpl implements IBoardService {

    @Autowired
    private BoardMapper boardMapper;

    @Override
    public List<Board> selectByNum(Integer num) {
        if (num == null || num <= 0) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        return boardMapper.selectByNum(num);
    }

    @Override
    public void addOneArticleCountById(Long id) {
        if (id == null || id <= 0) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
        Board board = boardMapper.selectByPrimaryKey(id);
        if (board == null) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NOT_EXISTS));
        }

        Board updateBoard = new Board();
        updateBoard.setId(board.getId());
        updateBoard.setArticleCount(board.getArticleCount() + 1);
        int row = boardMapper.updateByPrimaryKeySelective(updateBoard);
        if (row != 1) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
    }

    @Override
    public Board selectById(Long id) {
        if (id == null || id <= 0) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
        return boardMapper.selectByPrimaryKey(id);
    }

    @Override
    public void subOneArticleCountById(Long id) {
        if (id == null || id <= 0) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
        Board board = boardMapper.selectByPrimaryKey(id);
        if (board == null) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NOT_EXISTS));
        }

        Board updateBoard = new Board();
        updateBoard.setId(board.getId());
        updateBoard.setArticleCount(board.getArticleCount() - 1);
        int row = boardMapper.updateByPrimaryKeySelective(updateBoard);
        if (row != 1) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
    }
}
