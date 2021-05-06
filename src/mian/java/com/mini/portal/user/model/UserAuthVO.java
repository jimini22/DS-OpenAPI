package com.mini.portal.user.model;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mini.portal.comm.constants.AuthoritiesConstants;

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
	
	
	/* 내부 사용 정의 */
	@JsonIgnore
	private RoleVO role;
	
	//@JsonIgnore
	//private Set<PersistentTokenVO> persistentTokens = new HashSet<>();
	
	@JsonIgnore
	private Set<String> authorities;
	
	public Set<String> getAuthorities() {
		fillAuthorities();
		return authorities;
	}

	private void fillAuthorities() {
		if (authorities == null || authorities.size() == 0) {
			authorities = Optional.ofNullable(getRole())
								  .map(role -> role.getAuthorities()
										  		   .stream()
										  		   .map(AuthorityVO::getCode)
										  		   .collect(Collectors.toSet()))
								  .orElse(new HashSet<>());
			authorities.add(AuthoritiesConstants.HASLOGIN);
			if (isAdmin) {
				authorities.add(AuthoritiesConstants.MANAGER);
				authorities.add(AuthoritiesConstants.ADMIN);
			} else if (Objects.equals(roleCode, "ROLE_OFFER_MANAGER")) {
				authorities.add(AuthoritiesConstants.MANAGER);
				authorities.add(AuthoritiesConstants.MANAGER_PROVIDER);
			} else if (Objects.equals(roleCode, "ROLE_USER_MANAGER")) {
				authorities.add(AuthoritiesConstants.MANAGER);
				authorities.add(AuthoritiesConstants.MANAGER_PARTNER);
			} else if (Objects.equals(roleCode, "ROLE_OFFER_MEMBER")) {
				authorities.add(AuthoritiesConstants.USER);
				authorities.add(AuthoritiesConstants.USER_PROVIDER);
			} else if (Objects.equals(roleCode, "ROLE_USER_MEMBER")) {
				authorities.add(AuthoritiesConstants.USER);
				authorities.add(AuthoritiesConstants.USER_PARTNER);
			} else if (Objects.equals(roleCode, "ROLE_USER_ASSOCIATE")) {
				authorities.add(AuthoritiesConstants.USER);
				authorities.add(AuthoritiesConstants.USER_ASSOCIATE);
			}
		}
	}
	
	/**
	 * @description : 특정 권한이 있는지 여부 체크
	 * @param authName
	 * @return
	 */
	public boolean hasAuthority(String authName) {
		fillAuthorities();
		return authorities.contains(authName);
	}
	
}
