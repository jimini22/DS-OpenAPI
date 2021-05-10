package com.mini.portal.board.controller.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mini.portal.board.model.CommentVO;
import com.mini.portal.board.service.CommentService;
import com.mini.portal.comm.constants.ResCode;
import com.mini.portal.comm.controller.BaseRestController;
import com.mini.portal.comm.model.RestResultVO;
import com.mini.portal.comm.support.annotaion.ActiveUser;
import com.mini.portal.comm.utils.SecurityUserUtils;
import com.mini.portal.user.model.UserAuthVO;

import lombok.extern.slf4j.Slf4j;

/**
 * com.mini.portal.board.controller.rest
 *		>> CommentRestController.java
 * @author	: 지민희
 * @since	: 2021. 5. 10
 * @description : 댓글 관련 rest controller
 * @version :
 * 	----------------------------------
 * 		수정일			수정내용
 * 	----------------------------------
 * 		2021. 5. 10		최초 생성
 */
@RestController
@Slf4j
@RequestMapping(value = "/rest/comments")
public class CommentRestController extends BaseRestController {

	@Autowired
	private CommentService commentService;
	
	
	/**
	 * @description : 댓글 조회
	 * @param vo
	 * @param postId
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/all/{postId}")
	public RestResultVO<CommentVO> getAllComments ( @Valid CommentVO vo,
													@PathVariable("postId") Long postId ) throws Exception {
		
		vo.setPostId(postId);
		CommentVO resVO = commentService.getAllComments(vo);
		log.info(":::::::::::::::: 댓글 목록 조회");
		
		RestResultVO<CommentVO> resultVO = new RestResultVO<CommentVO>(ResCode.CD2000.getCode(), ResCode.CD2000.getMessage(), resVO);
		return resultVO;
		
	}
	
	/**
	 * @description : 댓글 등록ㅒ
	 * @param vo
	 * @param activeUser
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/write")
	public RestResultVO<CommentVO> writeComment ( @Valid @RequestBody CommentVO vo,
												@ActiveUser UserAuthVO activeUser ) throws Exception {
		
		activeUser = SecurityUserUtils.getCurrentUserInfo();
		
		if (activeUser != null) {
			vo.setWriter(activeUser.getLoginId());
			vo.setWriterId(activeUser.getId());
		}
		
		commentService.createComment(vo);
		log.info(":::::::::::::::: 댓글 등록");
			
		return new RestResultVO<CommentVO>(ResCode.CD2000.getCode(), ResCode.CD2000.getMessage());
		
	}
}
