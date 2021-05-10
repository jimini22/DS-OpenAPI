package com.mini.portal.board.model;

import java.util.List;

import lombok.Data;

@Data
public class CommentVO {

	private Long id;
	private Long postId;
	private String content;
	private String writer;
	private Long writerId;
	private String createdDate;
	private String userRoleCode;
	private String userOrganizationId;
	
	private List<CommentVO> commentList;
	private int commentTotCnt;
	
}
