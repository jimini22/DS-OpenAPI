package com.mini.portal.comm.utils;

import java.util.Random;
import java.util.StringTokenizer;

/**
 * com.mini.portal.comm.utils
 *		>> StringUtil.java
 * @author	: 지민희
 * @since	: 2021. 4. 29
 * @description : 문자열 처리 관련 util
 * @version :
 * 	----------------------------------
 * 		수정일			수정내용
 * 	----------------------------------
 * 		2021. 4. 29		최초 생성
 */
public class StringUtil {

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}
	
	public static boolean isNotEmpty(String str) {
		return !(str == null || str.length() == 0);
	}
	
	public static boolean isNumeric(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		if (sz == 0) {
			return false;
		}
		for (int i = 0; i < sz; i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	private static String null2void(String str) {
		if (isEmpty(str)) {
			str = "";
		}
		return str;
	}
	
	public static boolean equals(String source, String target) {
		return null2void(source).equals(null2void(target));
	}
	
	public static String commifyString(int num) {
		String str = String.valueOf(num);
		str = commifyString(str);
	
		return str;
	}
	
	public static String commifyString(String numTo) {
		String num = numTo;
		
		String spotFront = "";
		String spotAfter = "";
		String ret = "";
		String tmp = "";
		int len;
		
		if (num == null || num.equals("")) {
			ret = "0";
			return ret;
		} else {
			num = num.trim();
		}
		
		StringTokenizer tok = new StringTokenizer(num, ".");
		if (tok.countTokens() > 1) {
			spotAfter = num.substring(num.indexOf("."));
		}
		
		spotFront = tok.nextToken();
		if (spotFront.length() > 0  && spotFront.charAt(0) == '-') {
			ret = "-";
			spotFront = spotFront.substring(1);
		}
		
		len = spotFront.length();
		for (int i = 1; i <= len; i++) {
			tmp = spotFront.charAt(len-i) + tmp;
			if (i%3 == 0 && i < len) {
				tmp = "," + tmp;
			}
		}
		
		return String.format("%s%s%s", ret, tmp, spotAfter);
	}
	
	public static String appendString(Object...args) {
		if (args == null || args.length == 0) {
			return "";
		}
		
		StringBuffer ret = new StringBuffer();
				
		for (int i = 0; i < args.length; i++) {
			ret.append(args[i]);
		}
		
		return ret.toString();
	}
	
	public static String[] splitToken(String str, String strToken) {
		if (str.indexOf(strToken) != -1) {
			StringTokenizer st = new StringTokenizer(str, strToken);
			String[] stringArray = new String[st.countTokens()];
			for (int i = 0; st.hasMoreTokens(); i++) {
				stringArray[i] = st.nextToken();
			}
			return stringArray;
		}
		return new String[] {str};
	}
	
	/**
	 * @description : 비밀번호 초기화 시, 임시번호 발급 함수
	 * @param pwdSize
	 * @return
	 */
	public static String createRandomPassword(int pwdSize) {
		
		String pwd = "";
		char[] randomUnits = new char[] {
			'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
		};
		
		if (pwdSize > 0) {
			for (int i = 0; i < pwdSize; i++) {
				Random r = new Random();
				int pos = (int) (r.nextFloat() * randomUnits.length);
				pwd += randomUnits[pos];
			}
		}
		
		return pwd;
	}
	
	public static String replaceXssFilter(String str) {
		
		String codeRegex = "(\\/)?(script|applet|iframe|meta|svg|object|frameset|embed|link)(.*?)>";
		str = str.replaceAll(codeRegex, "&lt;$1$2$3&gt;");
		
		String imgRegx = "(?i)<(body|frame|img|input|link|style)(.*?)(onerror|onload|onclick)(.*?)>";
		str = str.replaceAll(imgRegx, "<$1$2no-$3$4>");
		
		return str;
	}
	
}
