package com.mini.portal.comm.model;

import java.util.List;

import lombok.Data;

@Data
public class AttachFileResVO {

	private List<AttachFileVO> fileList;
	
	private int totCnt;
	
	private PageInfoVO pageInfo;
	
}
