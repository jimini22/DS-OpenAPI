package com.mini.portal.comm.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * com.mini.portal.comm.model
 *		>> AttachFileReqVO.java
 * @author	: 지민희
 * @since	: 2021. 4. 30
 * @description : 첨부파일 정보 value object
 * @version :
 * 	----------------------------------
 * 		수정일			수정내용
 * 	----------------------------------
 * 		2021. 4. 30		최초 생성
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AttachFileReqVO extends AttachFileVO {

	/**
	 * 요청 페이지 인덱스
	 */
	private int pageNo = 1;
	
	/**
	 * 페이지 당 요청 데이터 수, 기본 10
	 */
	private int pageSize = Integer.MAX_VALUE;
	
	public void setPageNo(int pageNo) {
		if (pageNo < 1) {
			this.pageNo = 0;
		} else {
			this.pageNo = pageNo;
		}
	}
	
	public void setPageSize(int pageSize) {
		if (pageSize == 0) {
			this.pageSize = 10;
		} else {
			this.pageSize = pageSize;
		}
	}
	
	/**
	 * @description : 쿼리 실행시 전달할 시작 레코드 인덱스를 계산하여 반환한다
	 * @return
	 */
	public int getStartNo() {
		if (pageNo < 1) {
			return 0;
		}
		return (pageNo - 1) * pageSize;
	}
}
