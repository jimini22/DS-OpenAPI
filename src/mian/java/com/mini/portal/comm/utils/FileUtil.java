package com.mini.portal.comm.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;

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
	
	/**
	 * @description : 파일의 일시를 현재 일시로 변경한다
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
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
	
	/**
	 * @description : 파일을 읽는다
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static String readFile(File file) throws IOException {
		
		BufferedInputStream in = null;
		String ret = null;
		
		try {
			in = new BufferedInputStream(new FileInputStream(file));
			ret = readFileContent(in);
		} catch (Exception e) {
			
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				
			}
		}
		return ret;
	}
	
	/**
	 * @description : String 형으로 파일의 내용을 읽는다
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static String readFileContent(InputStream in) throws IOException {
		StringBuffer buff = new StringBuffer();
		
		for (int i = in.read(); i != -1; i = in.read()) {
			buff.append((char) i);
		}
		
		return buff.toString();
	}
	
	/**
	 * @description : String 형으로 파일의 내용을 읽는다
	 * @param file
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public static String readFile(File file, String encoding) throws IOException {
		StringBuffer buff = new StringBuffer();
		
		List<String> lines = FileUtils.readLines(file, encoding);
		
		if (lines != null && lines.size() > 0) {
			for (int i = 0; i < lines.size(); i++) {
				buff.append(lines.get(i));
				if (i < lines.size()-1) {
					buff.append("");
				}
			}
		}
		return buff.toString();
	}
	
	/**
	 * @description : 텍스트 내용을 파일로 쓴다
	 * @param file
	 * @param text
	 */
	public static void writeFile(File file, String text) {
		FileWriter writer = null;
		
		try {
			writer = new FileWriter(file);
			writer.write(text);
		} catch (Exception e) {
			log.error("Error creating File : {}", file.getName(), e);
			return;
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (Exception e) {
				
			}
		}
	}
	
	/**
	 * @description : 텍스트 내용을 파일로 쓴다
	 * @param fileName
	 * @param text
	 */
	public static void writeFile(String fileName, String text) {
		writeFile(new File(fileName), text);
	}
	
	/**
	 * @description : byte 데이터를 받아 파일에 저장한다
	 * @param filePath
	 * @param bytes
	 * @param append
	 * @return
	 */
	public static int writeFile(String filePath, byte[] bytes, boolean append) {
		
		int res = -1;
		ByteArrayInputStream	in = null;
		FileOutputStream		out = null;
		
		try {
			in = new ByteArrayInputStream(bytes);
			out = new FileOutputStream(filePath, append);
			
			int length = bytes.length;
			byte buff[] = new byte[length];
			
			length = in.read(buff, 0, buff.length);
			out.write(buff, 0, length);
			out.flush();
			res = length;
		} catch (FileNotFoundException e) {
			return res;
		} catch (IOException e) {
			return res;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					
				}
			}
		}
		return res;
	}
	
	/**
	 * @description : 텍스트 데이터를 받아 파일에 저장한다
	 * @param filePath
	 * @param textData
	 * @param append
	 * @return
	 */
	public static int writeTextFile(String filePath, String textData, boolean append) {
		
		byte[] bytes = textData.getBytes();
		int res = writeFile(filePath, bytes, append);
		
		return res;
		
	}
	
	/**
	 * @description : 파일 저장
	 * @param fileName
	 * @param data
	 * @param encoding
	 * @throws Exception
	 */
	public static void writeFile(String fileName, String data, String encoding) throws IOException {
		FileUtils.writeStringToFile(new File(fileName), data, encoding);
	}
	
	/**
	 * @description : byte 형으로 파일의 내용을 읽어온다
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static byte[] getContent(final FileObject file) throws IOException {
		final FileContent content = file.getContent();
		final int size = (int) content.getSize();
		final byte[] bytes = new byte[size];
		
		final InputStream in = content.getInputStream();
		try {
			int read = 0;
			for (int pos = 0; pos < size && read >= 0; pos += read) {
				read = in.read(bytes, pos, size-pos);
			}
		} finally {
			in.close();
		}
		
		return bytes;
	}
	
	/**
	 * @description : 내용을 파일에 OutputStream으로 저장한다
	 * @param file
	 * @param outstr
	 * @throws Exception
	 */
	public static void writeContent(FileObject file, OutputStream outstr) throws IOException {
		InputStream instr = file.getContent().getInputStream();
		try {
			byte[] bytes = new byte[1024];
			while (true) {
				int nread = instr.read(bytes);
				if (nread < 0) {
					break;
				}
				outstr.write(bytes, 0, nread);
			}
		} finally {
			instr.close();
		}
	}
	
	/**
	 * @description : 파일 객체를 대상 파일 객체로 복사한다
	 * @param srcFile
	 * @param desFile
	 * @throws IOException
	 */
	public static void copyContent(FileObject srcFile, FileObject desFile) throws IOException {
		OutputStream outstr = null;
		try {
			outstr = desFile.getContent().getOutputStream();
			writeContent(srcFile, outstr);
		} finally {
			try {
				if (outstr != null) {
					outstr.close();
				}
			} catch (Exception e) {
				
			}
		}
	}
	
	/**
	 * @description : 지정한 파일을 삭제한다
	 * @param file
	 * @throws IOException
	 */
	public static void deleteFile(File file) throws IOException {
		if (file.isDirectory()) {
			// it's a folder, list children first
			File[] children = file.listFiles();
			for (int i = 0; i < children.length; i++) {
				deleteFile(children[i]);
			}
		}
		if (!file.delete()) {
			throw new IOException("Unable to delete " + file.getPath());
		}
	}
	
	/**
	 * @description : 지정한 위치의 파일 및 디렉토리를 삭제한다
	 * @param filePath
	 * @throws IOException
	 */
	public static void deleteFilePath(String filePath) throws IOException {
		deleteFile(new File(filePath));
	}
	
	/**
	 * @description : 텍스트 파일을 읽어온다
	 * @param fileName
	 * @param newline
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static StringBuffer readTextFile(String fileName, boolean newline) throws FileNotFoundException, IOException {
		File file = new File(fileName);
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		
		StringBuffer buff = new StringBuffer();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(file));
			
			String str = in.readLine();
			while (str != null) {
				buff.append(str);
				if (newline) {
					buff.append(System.getProperty("line.separator"));
				}
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					
				}
			}
		}
		
		return buff;
	}
	
	/**
	 * @description : 특정 위치의 파일 객체를 가져온다
	 * @param filepath
	 * @return
	 * @throws Exception
	 */
	public static FileObject getFileObject(final String filepath) throws Exception {
		FileSystemManager mng = VFS.getManager();
		
		return mng.resolveFile(mng.resolveFile(System.getProperty("user.dir")), filepath);
	}
	
	/**
	 * @description : 폴더 존재 여부 검사하여, 없으면 생성 후 경로를 반환한다
	 * @param fullpath
	 * @throws Exception
	 */
	public static void createFolder(String fullpath) throws Exception {
		File dir = new File(fullpath);
		if (!dir.exists()) {
			dir.setReadable(true);
			dir.setWritable(true);
			dir.mkdirs();
		}
	}
	
	/**
	 * @description : 파일의 크기를 가져온다
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static long getFileSize(String fileName) throws Exception {
		File file = new File(fileName);
		if (!file.exists()) {
			return Long.valueOf("0");
		}
		return file.length();
	}
	
	
	/**
	 * @description : 파일의 확장자명을 추출한다
	 * @param strFileName
	 * @return
	 * @throws Exception
	 */
	public static String getFileExt(String strFileName) throws Exception {
		
		/* 값의 null 체크 */
		if (StringUtil.isEmpty(strFileName)) {
			return null;
		}
		
		/* 결과값 */
		String result = null;
		
		/* path가 없으므로 바로 반환한다 */
		if (strFileName.lastIndexOf(".") == -1) {
			return result;
		}
		
		/* 파일명만 추출 */
		result = strFileName.substring(strFileName.lastIndexOf(".") + 1);
		
		return result;
	}
	

	/**
	 * @description : 임시 파일명을 생성하여 반환한다
	 * @param originalFileName
	 * @return
	 * @throws Exception
	 */
	public static String createTempFileName(String originalFileName) throws Exception {
		
		if (StringUtil.isEmpty(originalFileName)) {
			return null;
		}
		
		String ext = getFileExt(originalFileName);
		
		/* 파일명 생성 */
		long cTime = System.currentTimeMillis();
		String cTimeStr = Long.valueOf(cTime).toString();
		int rNum = FileUtil.roledice();
		String rNumStr = Integer.valueOf(rNum).toString();
		
		StringBuffer tempFileName = new StringBuffer(cTimeStr);
		tempFileName.append(rNumStr);
		tempFileName.append(".");
		tempFileName.append(ext);
		
		return null;
	}
	
	private static int roledice() {
		Random r = new Random();
		// setSeed() 메소드를 사용해서 r을 예측 불가능한 long타입으로 설정한다
		r.setSeed(new Date().getTime());
		// 난수 생성
		return (r.nextInt()%6) + 1;
	}
	
	/**
	 * @description : make directory
	 * @param rootDir
	 */
	public static void mkdir(String rootDir) {
		File root = new File(rootDir);
		
		if (!root.isDirectory()) {
			root.mkdirs();
		}
	}
	
	/**
	 * @description : 파일을 다른 파일로 복사한다
	 * @param srcFilePath
	 * @param desFilePath
	 * @return
	 */
	public static boolean copyFile(String srcFilePath, String desFilePath) {
		boolean res = false;
		
		File srcFile = new File(srcFilePath);
		File desFile = new File(desFilePath);
		
		res = copyFile(srcFile, desFile);
		
		return res;
	}
	
	public static boolean copyFile(File srcFile, File desFile) {
		
		boolean res = false;
		FileInputStream istream = null;
		FileOutputStream ostream = null;
		FileChannel input = null;
		FileChannel output = null;
		
		try {
			istream = new FileInputStream(srcFile);
		} catch (FileNotFoundException e) {
			return res;
		}
		
		File p = desFile.getParentFile();
		if (p.exists() == false) {
			p.mkdirs();
		}
		try {
			ostream = new FileOutputStream(desFile);
			
			input = istream.getChannel();
			output = ostream.getChannel();
			
			// copy
			input.transferTo(0, input.size(), output);
			res = true;
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			
		} finally {
			if (istream != null) {
				try {
					istream.close();
				} catch (Exception e2) {
					
				}
			}
			if (ostream != null) {
				try {
					ostream.close();
				} catch (Exception e) {
					
				}
			}
			if (input != null) {
				try {
					input.close();
				} catch (Exception e) {
					
				}
			}
			if (output != null) {
				try {
					output.close();
				} catch (Exception e) {
					
				}
			}
		}
		return res;
	}
}
