package com.mini.portal.board.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mini.portal.board.mapper.CommentMapper;
import com.mini.portal.board.model.CommentVO;
import com.mini.portal.board.service.CommentService;

/**
 * com.mini.portal.board.service.impl
 *		>> CommentServiceImpl.java
 * @author	: 지민희
 * @since	: 2021. 5. 10
 * @description : 댓글 관련 서비스 구현
 * @version :
 * 	----------------------------------
 * 		수정일			수정내용
 * 	----------------------------------
 * 		2021. 5. 10		최초 생성
 */
@Service("CommentService")
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentMapper commentMapper;
	
	
	@Override
	public CommentVO getAllComments(CommentVO vo) throws Exception {
		
		CommentVO resVO = new CommentVO();
		
		List<CommentVO> commentList = commentMapper.getAllComments(vo);
		resVO.setCommentList(commentList);
		
		return resVO;
		
	}
	
	@Override
	public void createComment(CommentVO vo) throws Exception {
		
		commentMapper.createComment(vo);
		
	}
}
