package com.mini.portal.board.model;

import java.util.List;

import com.mini.portal.comm.model.PageInfoVO;

import lombok.Data;

@Data
public class PostResponseVO {
	
	private List<PostVO> postList;
	
	private List<PostVO> fixedList;
	
	private List<PostVO> replyList;
	
	private PostVO post;
	
	private int totCnt;
	
	private PageInfoVO pageInfo;
	
}
