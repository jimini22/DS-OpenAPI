package com.mini.portal.user.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * com.mini.portal.user.model
 *		>> RoleVO.java
 * @author	: 지민희
 * @since	: 2021. 5. 6
 * @description : 사용자 role value object class
 * @version :
 * 	----------------------------------
 * 		수정일			수정내용
 * 	----------------------------------
 * 		2021. 5. 6		최초 생성
 */
@Data
public class RoleVO implements Serializable {

	private static final long serialVersionUID = -5606476229219084513L;
	
	private String code;
	private String name;
	private String description;
	
	/**
	 * role에 해당하는 사용자 목록
	 */
	private List<UserAuthVO> users;
	
	/**
	 * role에 할당되어 있는 권한 정보 목록
	 */
	private List<AuthorityVO> authorities;
}
