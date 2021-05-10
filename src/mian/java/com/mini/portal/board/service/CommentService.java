package com.mini.portal.board.service;

import com.mini.portal.board.model.CommentVO;

/**
 * com.mini.portal.board.service
 *		>> CommentService.java
 * @author	: 지민희
 * @since	: 2021. 5. 10
 * @description : 댓글 관련 service interface
 * @version :
 * 	----------------------------------
 * 		수정일			수정내용
 * 	----------------------------------
 * 		2021. 5. 10		최초 생성
 */
public interface CommentService {

	/**
	 * @description : 댓글 목록 가져오기
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public CommentVO getAllComments(CommentVO vo) throws Exception;
	
	/**
	 * @description : 댓글 등록
	 * @param vo
	 * @throws Exception
	 */
	public void createComment(CommentVO vo) throws Exception;
	
}
