package com.mini.portal.board.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mini.portal.board.mapper.PostMapper;
import com.mini.portal.board.model.PostResponseVO;
import com.mini.portal.board.model.PostSearchVO;
import com.mini.portal.board.model.PostVO;
import com.mini.portal.board.service.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostMapper postMapper;
	
	@Override
	public PostResponseVO selectPostDetail(PostVO vo) throws Exception {
		
		PostResponseVO resVO = new PostResponseVO();
		
		PostVO post = postMapper.selectPostDetail(vo);
		resVO.setPost(post);
		
		return resVO;
	}
	
	@Override
	public Long createPost(PostVO vo) throws Exception {
		postMapper.createPost(vo);
		return vo.getId();
	}
	
	@Override
	public Long updatePost(PostVO vo) throws Exception {
		postMapper.updatePost(vo);
		return vo.getId();
	}
	
	@Override
	public void deletePost(PostVO vo) throws Exception {
		postMapper.deletePost(vo);
	}
	
	@Override
	public void hitPlus(Long id) throws Exception {
		postMapper.hitPlus(id);
	}
	
	@Override
	public PostVO selectReplyList(PostVO vo) throws Exception {
		
		PostVO pvo = new PostVO();
		
		List<PostVO> replyList = postMapper.selectReplyList(vo);
		pvo.setReplyList(replyList);
		
		return pvo;
	}
	
	@Override
	public void createReply(PostVO vo) throws Exception {
		postMapper.createReply(vo);
	}
	
	@Override
	public PostSendVO sendMessageToOrganization(Long organizationId) throws Exception {
		
		PostSendVO sendVO = new PostSendVO();
		
		List<PostReceiverVO> receiverList = postMapper.selectProviderInfoForSms(organizationId);
		sendVO.setReceiverList(receiverList);
		
		return sendVO;
	}
	
	@Override
	public PostSendVO sendMessageToAdmin() throws Exception {
		
		PostSendVO sendVO = new PostSendVO();
		
		PostReceiverVO receiver = postMapper.selectAdminInfoForSms();
		sendVO.setReceiver(receiver);
		
		return sendVO;
	}
	
	@Override
	public PostSendVO sendMessageToUser(Long id) throws Exception {
		
		PostSendVO sendVO = new PostSendVO();
		
		PostReceiverVO receiver = postMapper.selectUserInfoForSms();
		sendVO.setReceiver(receiver);
		
		return sendVO;
	}
	
	@Override
	public PostResponseVO selectPostList(PostSearchVO svo) throws Exception {
		
		PostResponseVO resVO = new PostResponseVO();
		
		//게시물 리스트
		List<PostVO> postList = postMapper.selectPostList(svo);
		resVO.setPostList(postList);
		
		//게시물 총 갯수
		int totCnt = postMapper.listTotCnt(svo);
		resVO.setTotCnt(totCnt);
		
		//페이징 정보
		PageInfoVO pageInfo = new PageInfoVo(svo.getPageNo(), svo.getPageSize(), totCnt);
		resVO.setPageInfo(pageInfo);
		
		return resVO;
	}
	
	@Override
	public PostResponseVO selectNoticeList(PostSearchVO svo) throws Exception {
		
		PostResponseVO resVO = new PostResponseVO();
		
		//게시물 리스트
		List<PostVO> postList = postMapper.selectPostList(svo);
		resVO.setPostList(postList);
		
		//게시물 총 갯수
		int totCnt = postMapper.listTotCnt(svo);
		resVO.setTotCnt(totCnt);
		
		//상단고정 리스트
		List<PostVO> fixedList = postMapper.selectFixedList(svo);
		resVO.setFixedList(fixedList);
		
		//페이징 정보
		PageInfoVO pageInfo = new PageInfoVo(svo.getPageNo(), svo.getPageSize(), totCnt);
		resVO.setPageInfo(pageInfo);
		
		return resVO;
		
	}
	
}
