package com.shinhan.portal.board.model;

import lombok.Data;

@Data
public class PostSearchVO {
	
	private Long id;
	private String title;
	private String content;
	private Boolean isPriority;
	
	private Long writerId;
	private String userFullName;
	private String createdBy;
	private String createdDate;
	private String lastModifiedBy;
	private String lastModifiedDate;
	
	private Long boardId;
	private String boardType;
	private Long targetOrganizationId;
	
	private String keyword;
	private String replyState;
	
	private String searchType;
	
	private int pageNo;
	private int pageSize = 10;
	
	public int getStartNo() {
		if (pageNo < 1) {
			return 0;
		}
		return (pageNo - 1) * pageSize;
	}
	
}
