package com.mini.portal.sms.service;

import com.mini.portal.sms.model.SmsDataVO;
import com.mini.portal.sms.model.StdMsgSendVO;
import com.mini.portal.user.model.UserAuthVO;

/**
 * com.mini.portal.sms.service
 *		>> SmsService.java
 * @author	: 지민희
 * @since	: 2021. 5. 4
 * @description : SMS 발송 및 이력 조회 Service Interface
 * @version :
 * 	----------------------------------
 * 		수정일			수정내용
 * 	----------------------------------
 * 		2021. 5. 4		최초 생성
 */
public interface SmsService {

	/**
	 * @description : 받는 사용자를 지정하여 메시지를 발송하고 이력을 남긴다 (사용자가 가지고 있는 휴대폰 번호로 발송)
	 * @param user
	 * @param smsDataVO
	 * @param smsType
	 */
	void send(UserAuthVO user, SmsDataVO smsDataVO, String smsType);

	/**
	 * @description : 받는 사용자를 지정하여 메시지를 발송하고 이력을 남긴다 (사용자가 가지고 있는 휴대폰 번호로 발송)
	 * @param smsDataVO
	 * @param smsType
	 */
	void send(SmsDataVO smsDataVO, String smsType);
	
	/**
	 * @description : 전화번호에 발송된 가장 최근 이력을 가져온다
	 * @param cellPhone
	 * @return
	 */
	SmsDataVO findLastMessageByPhone(String cellPhone);
	
	/**
	 * @description : 사용자에게 발송된 가장 최근 이력을 가져온다
	 * @param user
	 * @return
	 */
	SmsDataVO findLastMessageByUser(UserAuthVO user);
	
	/**
	 * @description : 메시지 전송 std_msg_send 테이블 저장
	 * @param msgSendVO
	 * @return
	 */
	String insertMsgSendTable(StdMsgSendVO msgSendVO);
	
	/**
	 * @description : 메시지 전송 tb_sms_data 테이블 저장
	 * @param smsDataVO
	 */
	void insertSmsHistoryTable(SmsDataVO smsDataVO);
	
}
