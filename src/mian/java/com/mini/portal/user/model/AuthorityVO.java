package com.mini.portal.user.model;

import java.io.Serializable;

import lombok.Data;

/**
 * com.mini.portal.user.model
 *		>> AuthorityVO.java
 * @author	: 지민희
 * @since	: 2021. 5. 6
 * @description : 권한 value object class
 * @version :
 * 	----------------------------------
 * 		수정일			수정내용
 * 	----------------------------------
 * 		2021. 5. 6		최초 생성
 */
@Data
public class AuthorityVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String code;
	private String name;
	
}
