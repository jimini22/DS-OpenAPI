package com.mini.portal.board.model;

import java.util.List;

import lombok.Data;

@Data
public class BoardVO {

	private int brdId;
	private String brdTitle;
	private String brdDescription;
	private int brdOrder;
	private String createdBy;
	private String createdDate;
	private String modifiedBy;
	private String modifiedDate;
	
	private List<BoardVO> boardList;

}
