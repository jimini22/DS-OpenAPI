package com.shinhan.portal.board.controller.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.portal.board.model.BoardVO;
import com.shinhan.portal.board.service.BoardService;
import com.shinhan.portal.comm.constants.ResCode;
import com.shinhan.portal.comm.controller.BaseRestController;
import com.shinhan.portal.comm.model.RestResultVO;

import lombok.extern.slf4j.Slf4j;

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
