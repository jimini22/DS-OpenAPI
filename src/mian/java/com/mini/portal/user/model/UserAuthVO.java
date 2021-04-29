package com.mini.portal.user.model;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;

import lombok.Data;

/**
 * com.mini.portal.user.model
 *		>> UserAuthVO.java
 * @author	: 지민희
 * @since	: 2021. 4. 29
 * @description : 로그인한 사용자의 데이터가 담기는  value object
 * @version :
 * 	----------------------------------
 * 		수정일			수정내용
 * 	----------------------------------
 * 		2021. 4. 29		최초 생성
 */
@Data
public class UserAuthVO extends UserVO implements Serializable {

	private static final long serialVersionUID = 20267307221952220L;
	
	private String createdBy;
	
	private String lastModifiedBy;
	
	private ZonedDateTime lastModifiedDate;
	
	private String activationKey;
	
	
	private Boolean deleted = false;
	
	private Boolean isAdmin = false;
	
	private Boolean isDormant = false;
	
	private Boolean isManager = false;
	
	private Boolean isSms = false;
	
	private Instant lastLoginDate;
	
	private int loginFailCount = 0;
	
	private ZonedDateTime resetDate = null;
	
	
	private String roleCode;
	
	
	
}
