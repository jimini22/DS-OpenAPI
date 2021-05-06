package com.mini.portal.comm.utils;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.mini.portal.comm.constants.AuthoritiesConstants;
import com.mini.portal.user.model.UserAuthVO;

/**
 * com.mini.portal.comm.utils
 *		>> SecurityUserUtils.java
 * @author	: 지민희
 * @since	: 2021. 5. 6
 * @description : 사용자 인증 관련 상태 정보 util class
 * @version :
 * 	----------------------------------
 * 		수정일			수정내용
 * 	----------------------------------
 * 		2021. 5. 6		최초 생성
 */
public class SecurityUserUtils {

	/**
	 * @description : 현재 로그인한 사용자의 아이디를 반환한다 (미로그인시 null 반환)
	 * @return
	 */
	public static String getCurrentUserLogin() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		String userName = null;
		if (authentication != null) {
			if (authentication.getPrincipal() instanceof UserDetails) {
				UserDetails springSeurityUser = (UserDetails) authentication.getPrincipal();
				userName = springSeurityUser.getUsername();
			} else if (authentication.getPrincipal() instanceof String) {
				userName = (String) authentication.getPrincipal();
			}
		}
		return userName;
	}
	
	/**
	 * @description : 현재 로그인한 사용자의 아이디를 반환한다 (미로그인시 null 반환)
	 * @return
	 */
	public static UserAuthVO getCurrentUserInfo() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		if (authentication != null) {
			if (authentication.getDetails() instanceof UserAuthVO) {
				UserAuthVO userInfo = (UserAuthVO) authentication.getDetails();
				return userInfo;
			}
		}
		return null;
	}
	
	/**
	 * @description : 현재 사용자 인증 상태를 반환한다
	 * @return
	 */
	public static boolean isAuthenticated() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		if (authentication != null) {
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			if (authorities != null) {
				for (GrantedAuthority authority : authorities) {
					if (authority.getAuthority().equals(AuthoritiesConstants.ANONYMOUS)) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * @description : 현재 로그인한 사용자의 권한 여부를 반환한다 (security role)
	 * @param authority
	 * @return
	 */
	public static boolean isCurrentUserInRole(String authority) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		if (authentication != null) {
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			if (authorities != null) {
				return authorities.contains(new SimpleGrantedAuthority(authority));
			}
		}
		return false;
	}
}
