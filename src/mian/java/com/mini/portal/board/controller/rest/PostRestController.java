package com.mini.portal.board.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mini.portal.board.model.PostResponseVO;
import com.mini.portal.board.model.PostVO;
import com.mini.portal.board.service.PostService;
import com.mini.portal.comm.controller.BaseRestController;
import com.mini.portal.comm.model.RestResultVO;
import com.miniportal.comm.constants.ResCode;

import lombok.extern.slf4j.Slf4j;

/**
 * com.mini.portal.board.controller.rest
 *		>> PostRestController.java
 * @author	: 지민희
 * @since	: 2021. 4. 28
 * @version :
 * 	----------------------------------
 * 		수정일			수정내용
 * 	----------------------------------
 * 		2021. 4. 28		최초 생성
 */
@RestController
@Slf4j
@RequestMapping(value = "/rest/posts")
public class PostRestController extends BaseRestController {

	@Autowired
	private PostService postService;
	
 	
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
		PostResponseVO resVO = PostService.selectPostDetail(vo);
		postService.hitPlus(id);
		
		RestResultVO<PostResponseVO> resultVO = new RestResultVO<PostResponseVO>(ResCode.CD2000.getCode(), ResCode.CD2000.getMessage(), resVO);
		return resultVO;
	}
	
	
	
	@PostMapping(value = "/write")
	public RestResultVO<Long> createPost ( @Valid @RequestBody PostVO vo,
											@ActiveUser UserAuthVO activeUser ) throws Exception {
		
		vo.setWriterId(activeUser.getId());		
		
	}
	
	
}
