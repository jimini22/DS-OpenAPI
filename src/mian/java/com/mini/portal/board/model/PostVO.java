package com.mini.portal.board.model;

import java.util.List;

import lombok.Data;

@Data
public class PostVO {

	private Long id;
	private String title;
	private String content;
	private String createdBy;
	private String createdDate;
	private String lastModifiedBy;
	private String lastModifiedDate;
	private Boolean isPrivate;
	private Long viewCount;
	
	private Long boardId;
	private String boardType;
	private Long parentId;
	private Long targetOrganizationId;
	private Long writerId;
	
	private String fullName;
	private String email;
	private String division;
	private Boolean isPriority = false;
	
	private List<PostVO> replyList;
	private int replyTotCnt;
	private String replyState;
	
	private String boardTitle;
	private String orgName;
	
	private int attachFileTotCnt;
	
	private Long userId;
	private String userFullName;
	private String userRoleCode;
	
	
	
}
