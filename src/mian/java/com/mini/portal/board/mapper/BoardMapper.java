package com.mini.portal.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mini.portal.board.model.BoardVO;

@Mapper
public interface BoardMapper {
	
	public List<BoardVO> getBoardList(BoardVO vo) throws Exception;

}
