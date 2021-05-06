package com.mini.portal.comm.constants;

public class AuthoritiesConstants {

	// 운영자
    public static final String ADMIN = "ROLE_ADMIN";
    // 기관 관리자
    public static final String MANAGER = "ROLE_MANAGER";
    // 계열사 관리자
    public static final String MANAGER_PROVIDER = "ROLE_MANAGER_PROVIDER";
    // 제휴사 관리자
    public static final String MANAGER_PARTNER = "ROLE_MANAGER_PARTNER";
    // 일반 사용자
    public static final String USER = "ROLE_USER";
    // 계열사 일반 사용자
    public static final String USER_PROVIDER = "ROLE_USER_PROVIDER";
    // 제휴사 일반 사용자
    public static final String USER_PARTNER = "ROLE_USER_PROVIDER";

    // 로그인한 사용자
    public static final String HASLOGIN = "ROLE_HASLOGIN";
    // 준회원
    public static final String USER_ASSOCIATE = "ROLE_USER_ASSOCIATE";
    // 미인증 사용자
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    // 사용자 생성 시 기본 역할
    public static final String DEFAULT_ROLE_CODE = "ROLE_NEWBEE";

    private AuthoritiesConstants() {
    }
    
}
