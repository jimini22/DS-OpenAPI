package com.mini.portal.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mini.portal.board.model.CommentVO;

/**
 * com.mini.portal.board.mapper
 *		>> CommentMapper.java
 * @author	: 지민희
 * @since	: 2021. 5. 10
 * @description : 댓글 관련 mapper
 * @version :
 * 	----------------------------------
 * 		수정일			수정내용
 * 	----------------------------------
 * 		2021. 5. 10		최초 생성
 */
@Mapper
public interface CommentMapper {
	
	public List<CommentVO> getAllComments(CommentVO vo) throws Exception;
	
	public int getCommentTotalCount(CommentVO vo) throws Exception;
	
	public void createComment(CommentVO vo) throws Exception;
	
}
