package com.mini.portal.comm.model;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.mini.portal.comm.utils.FileUtil;
import com.mini.portal.comm.utils.StringUtil;

import lombok.Data;

@Data
public class AttachFileVO {
	
	/**
	 * 파일 고유 아이디
	 */
	private Long id;
	
	/**
	 * 파일 원본 이름
	 */
	private String fileName;
	
	/**
	 * 파일 크기
	 */
	private Long fileSize;
	
	/*
	-----liquibase로 MysqlDB table을 생성/수정하는 경우의 참조사항-----
	Lob은 mysql에서 무조건 blob(65kb)로 적용된다.
	blob(200,000,000)을 주어도 mysql에서는 blob(65kb)로만 적용된다.
	longblob 타입을 아래와 같이 정의해도 liquibase가 blob으로만 인식한다.
	따라서 changelog에서 직접 longblob이라고 수정해주어야 한다.
	*/
	
	/**
	 * 파일 바이트 배열
	 */
	private byte[] attachFile;
	
	/**
	 * 파일 저장 물리경로
	 */
	private String attachFilePath;
	
	/**
	 * 파일 첨부 유형
	 */
	private String attachType;
	
	/**
	 * 연결된 게시물 아이디
	 */
	private Long postId;
	
	/**
	 * 멀티파트 파일 객체
	 */
	private MultipartFile file;
	
	public void applyAttachFile(String attachFileRoot) throws Exception {
		
		if(file == null) {
			throw new IOException();
		}
		
		try {
			fileName = file.getOriginalFilename();
			fileSize = file.getSize();
			if (StringUtil.isEmpty(attachFileRoot)) {
				attachFile = file.getBytes();
			} else {
				attachFilePath = attachFileRoot + "/" + FileUtil.createTempFileName(fileName);
			}
					
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException();
		}
		
	}
	
}