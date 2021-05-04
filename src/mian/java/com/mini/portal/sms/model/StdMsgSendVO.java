package com.mini.portal.sms.model;

import lombok.Data;

@Data
public class StdMsgSendVO {

	/* 필수 입력 항목 */
	private String cmpMsgId;	// primary key
	private String wrtDttm;		// 작성 일자
	private String sndDttm;		// 발송 일자
	private String rcvPhnId;	// 수신 번호
	private String sndPhnId;	// 발신 번호
	private String callback;	// 응답 번호
	private String sndMsg;		// 발송 메시지
	
	/* 기본 항목 */
	private String msgGroupId = "0";
	private String cmpUsrId = "00000";
	private char odrFg = '2';
	private char msgGb = 'A';
	private char smsGb = '1';
	private String usedCd = "00";
	private int expireVal = 0;
	private char smsSt = '0';
	private int rsltVal = 99;
	private char sndGb = 'N';
	private String natCd = "82";
	private String contentGroupId = "0";
	private char resendGb = 'N';
	
	/* 선택 입력 항목 */
	private String subject;
	private String regSndDttm;
	private String regRcvDttm;
	private String cmpSndDttm;
	private String cmpRcvDttm;
	
}