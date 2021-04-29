package com.mini.portal.comm.support.annotaion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * com.mini.portal.comm.support.annotaion
 *		>> ActiveUser.java
 * @author	: 지민희
 * @since	: 2021. 4. 29
 * @description : 컨트롤러 메서드에 @ActiveUser 가 있을 때, 해당 VO에 로그인한 사용자 정보를 주입한다
 * @version :
 * 	----------------------------------
 * 		수정일			수정내용
 * 	----------------------------------
 * 		2021. 4. 29		최초 생성
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ActiveUser {

}
