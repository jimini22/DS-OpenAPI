package com.mini.portal.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mini.portal.board.model.PostReceiverVO;
import com.mini.portal.board.model.PostSearchVO;
import com.mini.portal.board.model.PostVO;

@Mapper
public interface PostMapper {

	/**
	 * @description : 전체 게시물 리스트
	 * @param svo
	 * @return
	 * @throws Exception
	 */
	public List<PostVO> selectPostList(PostSearchVO svo) throws Exception;
	
	/**
	 * @description : 게시물 총 갯수
	 * @param svo
	 * @return
	 * @throws Exception
	 */
	public int listTotalCount(PostSearchVO svo) throws Exception;
	
	/**
	 * @description : 고정 게시물 리스트
	 * @param svo
	 * @return
	 * @throws Exception
	 */
	public List<PostVO> selectFixedList(PostSearchVO svo) throws Exception;
	
	/**
	 * @description : 게시물 상세내용
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public PostVO selectPostDetail(PostVO vo) throws Exception;
	
	/**
	 * @description : 게시물 작성
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public Long createPost(PostVO vo) throws Exception;
	
	/**
	 * @description : 조회수 증가
	 * @param id
	 * @throws Exception
	 */
	public void hitPlus(Long id) throws Exception;
	
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
	 * @description : 댓글 리스트
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<PostVO> selectReplyList(PostVO vo) throws Exception;
	
	/**
	 * @description : 댓글 총 갯수
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int replyTotalCount(PostVO vo) throws Exception;
	
	/**
	 * @description : 댓글 등록
	 * @param vo
	 * @throws Exception
	 */
	public void createReply(PostVO vo) throws Exception;
	
	/**
	 * @description : SMS 수신 대상 (제공기관)
	 * @param organizationId
	 * @return
	 * @throws Exception
	 */
	public List<PostReceiverVO> selectProviderInfoForSms(Long organizationId) throws Exception;
	
	/**
	 * @description : SMS 수신 대상 (어드민)
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public PostReceiverVO selectAdminInfoForSms() throws Exception;
	
	/**
	 * @description : SMS 수신 대상 (유저 정보)
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public PostReceiverVO selectUserInfoForSms(Long id) throws Exception;
	
}
