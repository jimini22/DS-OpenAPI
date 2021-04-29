package com.mini.portal.board.controller.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mini.portal.board.model.PostResponseVO;
import com.mini.portal.board.model.PostVO;
import com.mini.portal.board.service.PostService;
import com.mini.portal.comm.constants.ResCode;
import com.mini.portal.comm.controller.BaseRestController;
import com.mini.portal.comm.mapper.AttachFileMapper;
import com.mini.portal.comm.model.RestResultVO;
import com.mini.portal.comm.support.annotaion.ActiveUser;
import com.mini.portal.user.model.UserAuthVO;

import lombok.extern.slf4j.Slf4j;

/**
 * com.mini.portal.board.controller.rest
 *		>> PostRestController.java
 * @author	: 지민희
 * @since	: 2021. 4. 29
 * @description : 게시물 관련 controller
 * @version :
 * 	----------------------------------
 * 		수정일			수정내용
 * 	----------------------------------
 * 		2021. 4. 29		최초 생성
 */
@RestController
@Slf4j
@RequestMapping(value = "/rest/posts")
public class PostRestController extends BaseRestController {

	@Autowired
	private PostService postService;
	
	@Autowired
	private AttachFileService attachFileService;
	
	@Autowired
	private AttachFileMapper attachFileMapper;
	
 	
	/**
	 * @description : 게시물 상세조회
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/detail/{id}")
	public RestResultVO<PostResponseVO> getPostDetail ( @PathVariable("id") Long id ) throws Exception {
		
		PostVO vo = new PostVO();
		vo.setId(id);
		PostResponseVO resVO = postService.selectPostDetail(vo);
		postService.hitPlus(id);
		log.info(":::::::::::::::: 게시글 상세정보 조회");
		
		RestResultVO<PostResponseVO> resultVO = new RestResultVO<PostResponseVO>(ResCode.CD2000.getCode(), ResCode.CD2000.getMessage(), resVO);
		return resultVO;
	}
	
	/**
	 * @description : 게시글 등록
	 * @param vo
	 * @param activeUser
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/write")
	public RestResultVO<Long> createPost ( @Valid @RequestBody PostVO vo,
											@ActiveUser UserAuthVO activeUser ) throws Exception {
		
		vo.setWriterId(activeUser.getId());		
		vo.setCreatedBy(activeUser.getLoginId());
		Long postId = postService.createPost(vo);
		log.info(":::::::::::::::: 게시글 등록");
		
		RestResultVO<Long> resultVO = new RestResultVO<Long>(ResCode.CD2000.getCode(), ResCode.CD2000.getMessage(), postId);
		return resultVO;
	}
	
	/**
	 * @description : 게시글 수정 실행
	 * @param vo
	 * @param activeUser
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/update/{id}")
	public RestResultVO<Long> updatePost ( @Valid @RequestBody PostVO vo,
											@ActiveUser UserAuthVO activeUser,
											@PathVariable("id") Long id ) throws Exception {
		 
		vo.setLastModifiedBy(activeUser.getLoginId());
		vo.setId(id);
		Long postId = postService.updatePost(vo);
		
		RestResultVO<Long> resultVO = new RestResultVO<Long>(ResCode.CD2000.getCode(), ResCode.CD2000.getMessage(), postId);
		return resultVO;
	}
	
	@PostMapping(value = "/delete/{id}")
	public RestResultVO<PostResponseVO> deletePost ( @Valid PostVO vo,
													@PathVariable("id") Long id ) throws Exception {
		
		AttachFileReqVO avo = new AttachFileReqVO();
		avo.setPostId(vo.getId());
		int fileCnt = attachFileMapper.selectAttachFileListCount(avo);
		
	}
	
	
}
