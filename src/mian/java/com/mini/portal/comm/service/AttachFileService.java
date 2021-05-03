package com.mini.portal.comm.service;

import java.util.List;

import com.mini.portal.comm.model.AttachFileReqVO;
import com.mini.portal.comm.model.AttachFileResVO;
import com.mini.portal.comm.model.AttachFileVO;

/**
 * com.mini.portal.comm.service
 *		>> AttachFileService.java
 * @author	: 지민희
 * @since	: 2021. 5. 3
 * @description : 첨부파일 정보 관련 Service interface
 * @version :
 * 	----------------------------------
 * 		수정일			수정내용
 * 	----------------------------------
 * 		2021. 5. 3		최초 생성
 */
public interface AttachFileService {

	/**
	 * @description : 첨부파일 목록 조회 인터페이스
	 * @param vo
	 * @return
	 */
	public AttachFileResVO selectAttachFileList(AttachFileReqVO vo);
	
	/**
	 * @description : 첨부파일 목록 조회 인터페이스 (페이징 없음)
	 * @param vo
	 * @return
	 */
	public List<AttachFileVO> selectAttachFileListAll(AttachFileVO vo);
	
	/**
	 * @description : 첨부파일 조회 인터페이스
	 * @param vo
	 * @return
	 */
	public AttachFileVO selectAttachFile(AttachFileVO vo);
	
	/**
	 * @description : 첨부파일 저장 인터페이스
	 * @param vo
	 * @return
	 */
	public int insertAttachFile(AttachFileVO vo);
	
	/**
	 * @description : 첨부파일 정보 수정 인터페이스
	 * @param vo
	 * @return
	 */
	public int updateAttachFile(AttachFileVO vo);
	
	/**
	 * @description : 첨부파일 삭제 인터페이스
	 * @param vo
	 * @return
	 */
	public int deleteAttachFile(AttachFileVO vo);
	
	/**
	 * @description : 첨부파일 삭제 인터페이스
	 * @param vo
	 * @return
	 */
	public Long deleteAttachFiles(AttachFileVO vo);
	
	/**
	 * @description : 첨부파일 저장
	 * @param filePath
	 * @param attachFile
	 * @throws Exception
	 */
	public void saveAttachFile(String filePath, byte[] attachFile) throws Exception;
}
