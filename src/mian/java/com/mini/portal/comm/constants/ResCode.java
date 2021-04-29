package com.mini.portal.comm.constants;

public enum ResCode {

	CD2000(2000, "SUCCESS"),
	CD4000(4000, "잘못된 요청입니다. 요청 변수 값이 없거나 올바르지 않습니다."),
	CD4001(4001, "사용자 로그인 인증이 필요합니다."),
	CD4003(4003, "사용자 접근 권한이 필요합니다."),
	CD4004(4004, "올바른 URI[{{uri}}]가 아닙니다."),
	CD4005(4005, "허용되지 않는 HTTP Method[{{method}}]입니다."),
	CD4006(4006, "요청한 MediaType[{{accept}}]을 지원하지 않습니다."),
	CD4015(4015, "요청한 MediaType[{{contentstype}}]을 지원하지 않습니다."),
	CD9000(9000, "서비스 요청 처리 중 오류가 발생하였습니다."),
	CD9010(9010, "데이터의 parsing에 실패했습니다."),
	CD9011(9011, "필수 데이터 값이 존재하지 않습니다."),
	CD9020(9020, "데이터 암호화 처리에 실패했습니다."),
	CD9021(9021, "데이터 복호화 처리에 실패했습니다."),
	CD9031(9031, "파일 읽기 처리에 실패했습니다."),
	CD9032(9032, "파일 쓰기 처리에 실패했습니다."),
	CD9033(9033, "파일 삭제 처리에 실패했습니다."),
	CD9030(9030, "SMS 인증에 실패했습니다."),
	CD9050(9050, "Database 연동 작업 중 오류가 발생하였습니다."),
	CD9100(9100, "HTTP 서버 연결에 실패했습니다.");
	
	ResCode(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	private final int code;
	
	private final String message;
	
	public int getCode() {
		return this.code;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public static ResCode fromValue(int code) {
		for (ResCode b : ResCode.values()) {
			if (b.code == code) {
				return b;
			}
		}
		return null;
	}
	
};
