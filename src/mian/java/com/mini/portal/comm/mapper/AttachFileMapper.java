package com.mini.portal.comm.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mini.portal.comm.model.AttachFileVO;

/**
 * com.mini.portal.comm.mapper
 *		>> AttachFileMapper.java
 * @author	: 지민희
 * @since	: 2021. 4. 29
 * @description : 첨부파일 정보 관련 mapper class
 * @version :
 * 	----------------------------------
 * 		수정일			수정내용
 * 	----------------------------------
 * 		2021. 4. 29		최초 생성
 */
@Repository /* @repository 와 @mapper 의 차이는??? */
public interface AttachFileMapper {

	/**
	 * @description : 첨부파일 전체 갯수
	 * @param vo
	 * @return
	 */
	public int selectAttachFileListCount(AttachFileVO vo);
	
	/**
	 * @description : 첨부파일 리스트 조회
	 * @param rvo
	 * @return
	 */
	public List<AttachFileVO> selectAttachFileList(AttachFileReqVO rvo);
	
	/**
	 * @description : 첨부파일 리스트 조회 (페이징 없음)
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
	 * @description : 첨부파일 정보 업데이트 인터페이스
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
	
}
