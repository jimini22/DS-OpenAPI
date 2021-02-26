package com.shinhan.portal.board.service;

import com.shinhan.portal.board.model.BoardVO;

public interface BoardService {

	public BoardVO getBoardList(BoardVO vo) throws Exception;	//게시판 조회

}
