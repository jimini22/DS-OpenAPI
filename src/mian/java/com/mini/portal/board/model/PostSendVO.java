package com.mini.portal.board.model;

import java.util.List;

import lombok.Data;

@Data
public class PostSendVO {

	private String smsType;
	
	private List<PostReceiverVO> receiverList;
	
	private PostReceiverVO receiver;
	
}
