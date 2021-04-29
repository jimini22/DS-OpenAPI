package com.mini.portal.comm.utils;

import javax.tools.FileObject;

import lombok.extern.slf4j.Slf4j;

/**
 * com.mini.portal.comm.utils
 *		>> FileUtil.java
 * @author	: 지민희
 * @since	: 2021. 4. 29
 * @description : 파일 관련 util
 * @version :
 * 	----------------------------------
 * 		수정일			수정내용
 * 	----------------------------------
 * 		2021. 4. 29		최초 생성
 */
@Slf4j
public class FileUtil {

	private static FileObject basefile;
	
	private static FileSystemManager manager;
	
	static {
		try {
			manager = VFS.getManager();
			basefile = manager.resolveFile(System.getProperty("user.dir"));
		} catch (FileSystemException e) {
			log.error("EgovFileUtil : " + e.getMessage());
		}
	}
	
	public static long touch(final String filePath) throws Exception {
		
		long currentTime = 0;
		FileObject file = manager.resolveFile(basefile, filePath);
		
		if (!file.exists()) {
			file.createFile();
		}
		currentTime = System.currentTimeMillis();
		file.getContent().setLastModifiedTime(currentTime);
		
		return currentTime;
	}
	
	
	
	
}
