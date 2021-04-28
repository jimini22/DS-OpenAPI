package com.mini.portal.board.controller.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mini.portal.board.model.BoardVO;
import com.mini.portal.board.service.BoardService;
import com.mini.portal.comm.controller.BaseRestController;
import com.mini.portal.comm.model.RestResultVO;
import com.miniportal.comm.constants.ResCode;
import com.sun.org.slf4j.internal.Logger;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import sun.util.logging.resources.logging;

@RestController
@Slf4j
@RequestMapping(value = "/rest/boards")
public class BoardRestController extends BaseRestController {

	@Autowired
	private BoardService boardService;
	
	@GetMapping(value = "/all/{id}")
	public RestResultVO<BoardVO> getBoardList ( @Valid BoardVO vo,
												@PathVariable("id") int brdId ) throws Exception {
		
		vo.setBrdId(brdId);
		BoardVO resVO = boardService.getBoardList(vo);
		log.info(":::::::::::::::: board 전체 목록 조회");
		
		RestResultVO<BoardVO> resultVO = new RestResultVO<BoardVO>(ResCode.CD2000.getCode(), ResCode.CD2000.getMessage(), resVO);
		
		return resultVO;
		
	}
}
