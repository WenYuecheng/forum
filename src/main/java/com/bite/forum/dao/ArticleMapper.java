package com.bite.forum.dao;

import com.bite.forum.model.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper {
    int insert(Article row);

    int insertSelective(Article row);

    Article selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Article row);

    int updateByPrimaryKeyWithBLOBs(Article row);

    int updateByPrimaryKey(Article row);

    List<Article> selectAll();

    List<Article> selectByBoardId(Long boardId);

    Article selectDetailById(Long id);

    List<Article> selectByUserId(Long userId);
}