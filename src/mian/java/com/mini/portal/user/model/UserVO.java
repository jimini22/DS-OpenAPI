package com.mini.portal.user.model;

import java.io.Serializable;
import java.time.Instant;

import lombok.Data;

@Data
public class UserVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected Long id;
	
	protected String loginId;
	
	protected String email;
	
	protected String fullName;
	
	protected String cellPhone;
	
	protected String passwordHash;
	
	protected Instant passwordModifiedDate;
	
	protected Long organizationId;
	
	//maskin util 관련 추가
}
