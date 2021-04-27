package com.shinhan.portal.board.controller.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.portal.board.model.PostResponseVO;
import com.shinhan.portal.board.model.PostVO;
import com.shinhan.portal.board.service.PostService;
import com.shinhan.portal.comm.controller.BaseRestController;
import com.shinhan.portal.comm.model.RestResultVO;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/rest/posts")
public class PostRestController extends BaseRestController {

	
	@GetMapping(value = "/detail/{id}")
	public RestResultVO<PostResponseVO> getPostDetail ( @PathVariable("id") Long id ) throws Exception {
		
		PostVO vo = new PostVO();
		vo.setId(id);
		PostResponseVO resVO = PostService.selectPostDetail(vo);
	}
}
