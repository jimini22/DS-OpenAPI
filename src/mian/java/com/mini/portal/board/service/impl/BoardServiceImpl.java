package com.mini.portal.board.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mini.portal.board.mapper.BoardMapper;
import com.mini.portal.board.model.BoardVO;
import com.mini.portal.board.service.BoardService;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	private BoardMapper boardMapper;
	
	
	@Override
	public BoardVO getBoardList(BoardVO vo) throws Exception {
		
		BoardVO resVO = new BoardVO();
		
		List<BoardVO> boardList = boardMapper.getBoardList(vo);
		resVO.setBoardList(boardList);
		
		return resVO;
	}
	

}
