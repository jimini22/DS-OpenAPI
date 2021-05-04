package com.mini.portal.sms.model;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;

import lombok.Data;

/**
 * com.mini.portal.sms.model
 *		>> SmsDataVO.java
 * @author	: 지민희
 * @since	: 2021. 5. 4
 * @description : SMS 발송 요청 및 이력을 위한 Value Object Class
 * @version :
 * 	----------------------------------
 * 		수정일			수정내용
 * 	----------------------------------
 * 		2021. 5. 4		최초 생성
 */
@Data
public class SmsDataVO {

	private Long id;
	private Long userId;
	private String loginId;
	private String cellPhone;
	private String email;
	private String message;
	private String authNumber;
	private Map<String, Object> messageMap;
	private String requestMessageId;
	private boolean isSent = false;
	private boolean isFinished = false;
	private boolean isTimeover = false;
	private String requestResult;
	private String requestErrorCode;
	private String resultString;
	private String resultErrorCode;
	private ZonedDateTime resultSentDate;
	
	public String toString() {
		return String.format("= SMS 전송 정보 =\n번호 : %s\n내용 : %s\n=============", Objects.isNull(getCellPhone()) ? "없음" : getCellPhone(), message);
	}
	
}
