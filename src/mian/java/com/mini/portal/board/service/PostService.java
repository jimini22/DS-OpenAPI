package com.mini.portal.board.service;

import com.mini.portal.board.model.PostResponseVO;
import com.mini.portal.board.model.PostSearchVO;
import com.mini.portal.board.model.PostSendVO;
import com.mini.portal.board.model.PostVO;

/**
 * com.mini.portal.board.service
 *		>> PostService.java
 * @author	: 지민희
 * @since	: 2021. 5. 10
 * @description : 게시글 관련 service interface
 * @version :
 * 	----------------------------------
 * 		수정일			수정내용
 * 	----------------------------------
 * 		2021. 5. 10		최초 생성
 */
public interface PostService {

	/**
	 * @description : 게시물 상세내용
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public PostResponseVO selectPostDetail(PostVO vo) throws Exception;
		
	/**
	 * @description : 게시물 글쓰기
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public Long createPost(PostVO vo) throws Exception;
	
	/**
	 * @description : 게시물 수정
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public Long updatePost(PostVO vo) throws Exception;
	
	/**
	 * @description : 게시물 삭제
	 * @param vo
	 * @throws Exception
	 */
	public void deletePost(PostVO vo) throws Exception;
	
	/**
	 * @description : 조회수 증가
	 * @param id
	 * @throws Exception
	 */
	public void hitPlus(Long id) throws Exception;
	
	/**
	 * @description : 댓글 리스트
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public PostVO selectReplyList(PostVO vo) throws Exception;
		
	/**
	 * @description : 댓글 등록
	 * @param vo
	 * @throws Exception
	 */
	public void createReply(PostVO vo) throws Exception;
	
	/**
	 * @description : 메시지 전송 (제공기관)
	 * @param organizationId
	 * @return
	 * @throws Exception
	 */
	public PostSendVO sendMessageToOrganization(Long organizationId) throws Exception;
	
	/**
	 * @description : 메시지 전송 (어드민)
	 * @return
	 * @throws Exception
	 */
	public PostSendVO sendMessageToAdmin() throws Exception;
	
	/**
	 * @description : 메시지 전송 (유저)
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public PostSendVO sendMessageToUser(Long id) throws Exception;
	
	/**
	 * @description : 게시물 전체 리스트 조회
	 * @param svo
	 * @return
	 * @throws Exception
	 */
	public PostResponseVO selectPostList(PostSearchVO svo) throws Exception;
	
	/**
	 * @description : 게시물 고정 리스트 조회
	 * @param svo
	 * @return
	 * @throws Exception
	 */
	public PostResponseVO selectNoticeList(PostSearchVO svo) throws Exception;
	
}
