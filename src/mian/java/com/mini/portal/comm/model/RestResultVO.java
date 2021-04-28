package com.mini.portal.comm.model;

import com.miniportal.comm.constants.ResCode;

import lombok.Data;

@Data
public class RestResultVO<T> {
	
	private int code;
	
	private String message;
	
	private T data;
	
	public RestResultVO() {
		code = ResCode.CD2000.getCode();
		message = ResCode.CD2000.getMessage();
		data = null;
	}
	
	public RestResultVO(int code, String message) {
		this.code = code;
		this.message = message;
		data = null;
	}
	
	public RestResultVO(ResCode resCode) {
		this.code = resCode.getCode();
		this.message = resCode.getMessage();
		data = null;
	}
	
	public RestResultVO(int code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}
	
	public RestResultVO(ResCode resCode, T data) {
		this.code = resCode.getCode();
		this.message = resCode.getMessage();
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "RestResultVO [code=" + code + ", message=" + message + ", data=" + data + "]";
	}
	
	
	
}
