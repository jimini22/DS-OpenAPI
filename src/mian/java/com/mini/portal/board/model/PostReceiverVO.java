package com.mini.portal.board.model;

import lombok.Data;

@Data
public class PostReceiverVO {

	private Long id;
	private String cellPhone;
	private String email;
	private String fullName;
	private String loginId;
	private String roleCode;
	private Boolean isSms;
	
}
