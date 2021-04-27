package com.shinhan.portal.board.model;

import java.util.List;

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
