package com.mini.portal.board.controller.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mini.portal.board.model.PostReceiverVO;
import com.mini.portal.board.model.PostResponseVO;
import com.mini.portal.board.model.PostSendVO;
import com.mini.portal.board.model.PostVO;
import com.mini.portal.board.service.PostService;
import com.mini.portal.comm.constants.ResCode;
import com.mini.portal.comm.constants.SmsType;
import com.mini.portal.comm.controller.BaseRestController;
import com.mini.portal.comm.mapper.AttachFileMapper;
import com.mini.portal.comm.model.AttachFileReqVO;
import com.mini.portal.comm.model.AttachFileResVO;
import com.mini.portal.comm.model.AttachFileVO;
import com.mini.portal.comm.model.RestResultVO;
import com.mini.portal.comm.service.AttachFileService;
import com.mini.portal.comm.support.annotaion.ActiveUser;
import com.mini.portal.sms.model.SmsDataVO;
import com.mini.portal.sms.service.SmsService;
import com.mini.portal.user.model.UserAuthVO;

import lombok.extern.slf4j.Slf4j;

/**
 * com.mini.portal.board.controller.rest
 *		>> PostRestController.java
 * @author	: 지민희
 * @since	: 2021. 4. 29
 * @description : 게시물 관련 controller
 * @version :
 * 	----------------------------------
 * 		수정일			수정내용
 * 	----------------------------------
 * 		2021. 4. 29		최초 생성
 */
@RestController
@Slf4j
@RequestMapping(value = "/rest/posts")
public class PostRestController extends BaseRestController {

	@Autowired
	private PostService postService;
	
	@Autowired
	private AttachFileService attachFileService;
	
	@Autowired
	private AttachFileMapper attachFileMapper;
	
	@Autowired
	private SmsService smsService;
	
 	
	/**
	 * @description : 게시물 상세조회
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/detail/{id}")
	public RestResultVO<PostResponseVO> getPostDetail ( @PathVariable("id") Long id ) throws Exception {
		
		PostVO vo = new PostVO();
		vo.setId(id);
		PostResponseVO resVO = postService.selectPostDetail(vo);
		postService.hitPlus(id);
		log.info(":::::::::::::::: 게시글 상세정보 조회");
		
		RestResultVO<PostResponseVO> resultVO = new RestResultVO<PostResponseVO>(ResCode.CD2000.getCode(), ResCode.CD2000.getMessage(), resVO);
		return resultVO;
	}
	
	/**
	 * @description : 게시글 등록
	 * @param vo
	 * @param activeUser
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/write")
	public RestResultVO<Long> createPost ( @Valid @RequestBody PostVO vo,
											@ActiveUser UserAuthVO activeUser ) throws Exception {
		
		vo.setWriterId(activeUser.getId());		
		vo.setCreatedBy(activeUser.getLoginId());
		Long postId = postService.createPost(vo);
		log.info(":::::::::::::::: 게시글 등록");
		
		RestResultVO<Long> resultVO = new RestResultVO<Long>(ResCode.CD2000.getCode(), ResCode.CD2000.getMessage(), postId);
		return resultVO;
	}
	
	/**
	 * @description : 게시글 수정 실행
	 * @param vo
	 * @param activeUser
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/update/{id}")
	public RestResultVO<Long> updatePost ( @Valid @RequestBody PostVO vo,
											@ActiveUser UserAuthVO activeUser,
											@PathVariable("id") Long id ) throws Exception {
		 
		vo.setLastModifiedBy(activeUser.getLoginId());
		vo.setId(id);
		Long postId = postService.updatePost(vo);
		
		RestResultVO<Long> resultVO = new RestResultVO<Long>(ResCode.CD2000.getCode(), ResCode.CD2000.getMessage(), postId);
		return resultVO;
	}
	
	/**
	 * @description : 게시글 삭제 실행
	 * @param vo
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/delete/{id}")
	public RestResultVO<PostResponseVO> deletePost ( @Valid PostVO vo,
													@PathVariable("id") Long id ) throws Exception {
		
		AttachFileReqVO avo = new AttachFileReqVO();
		avo.setPostId(vo.getId());
		int fileCnt = attachFileMapper.selectAttachFileListCount(avo);
		
		if (fileCnt > 0) {
			AttachFileVO attachFileVO = new AttachFileVO();
			attachFileVO.setPostId(vo.getId());
			List<AttachFileVO> attachList = attachFileService.selectAttachFileListAll(attachFileVO);
			attachList.forEach(attachDeleteFile -> {
				attachFileService.deleteAttachFile(attachDeleteFile);
			});
			log.info(":::::::::::::::: 게시글에 포함된 첨부파일 삭제 완료");
		}
		
		vo.setId(id);
		postService.deletePost(vo);
		log.info(":::::::::::::::: 게시글 삭제 완료");
		
		RestResultVO<PostResponseVO> resultVO = new RestResultVO<PostResponseVO>(ResCode.CD2000.getCode(), ResCode.CD2000.getMessage());
		return resultVO;
	}
	
	/**
	 * @description : 댓글 목록 조회
	 * @param vo
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/reply/all/{id}")
	public RestResultVO<PostVO> getReplyList ( @Valid PostVO vo,
												@PathVariable("id") Long id ) throws Exception {
		
		vo.setId(id);
		PostVO resVO = postService.selectReplyList(vo);
		log.info(":::::::::::::::: 댓글 목록 조회");
		
		RestResultVO<PostVO> resultVO = new RestResultVO<PostVO>(ResCode.CD2000.getCode(), ResCode.CD2000.getMessage(), resVO);
		return resultVO;
	}
	
	/**
	 * @description : 댓글 등록
	 * @param vo
	 * @param activeUser
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/reply/write")
	public RestResultVO<PostResponseVO> createReply ( @Valid @RequestBody PostVO vo,
														@ActiveUser UserAuthVO activeUser ) throws Exception {
		
		vo.setWriterId(activeUser.getId());
		vo.setCreatedBy(activeUser.getLoginId());
		postService.createReply(vo);
		log.info(":::::::::::::::: 댓글 등록");
		
		RestResultVO<PostResponseVO> resultVO = new RestResultVO<PostResponseVO>(ResCode.CD2000.getCode(), ResCode.CD2000.getMessage());
		return resultVO;
	}
	
	
	/////////////////////////////////////////////////// 추후 공통으로 빼기 ///////////////////////////////////////////////////
	/**
	 * @description : 저장되어 있는 첨부파일 목록을 조회하여 반환한다
	 * @param reqVO
	 * @return
	 */
	@GetMapping(value = "/files/{postId}")
	public RestResultVO<AttachFileResVO> getAttachFileList ( @ModelAttribute AttachFileReqVO reqVO ) {
		
		AttachFileResVO fileListVO = attachFileService.selectAttachFileList(reqVO);
		
		RestResultVO<AttachFileResVO> resultVO = new RestResultVO<AttachFileResVO>(ResCode.CD2000.getCode(), ResCode.CD2000.getMessage(), fileListVO);
		return resultVO;
	}
	
	/**
	 * @description : is_sms가 true인 제공기관에게 sms 발송
	 * @param type
	 * @param organizationId
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/sms/{type}/{organizationId}")
	public RestResultVO<PostSendVO> sendSmsToOrg ( @PathVariable(name="type", required = true) String type,
													@PathVariable(name="organizationId", required = true) Long organizationId ) throws Exception {
		
		String smsType = "";
		if ("qna".equals(type)) {
			smsType = SmsType.BOARD_QUESTION_AND_ASK_POST;
		} else if ("question".equals(type)) {
			smsType = SmsType.BOARD_ONE_TO_ONE_QUESTION_POST;
		} else if ("business".equals(type)) {
			smsType = SmsType.BOARD_BUSINESS_PROPOSAL_POST;
		} else if ("qnareply".equals(type)) {
			smsType = SmsType.BOARD_QUESTION_AND_ASK_REPLY;
		} else if ("questionreply".equals(type)) {
			smsType = SmsType.BOARD_ONE_TO_ONE_QUESTION_REPLY;
		}
		
		PostSendVO sendVO = postService.sendMessageToOrganization(organizationId);
		List<PostReceiverVO> receiverList = sendVO.getReceiverList();
		
		if (receiverList.isEmpty()) {
			return new RestResultVO<PostSendVO>(ResCode.CD9041.getCode(), ResCode.CD9041.getMessage());
		}
		
		SmsDataVO smsDataVO = new SmsDataVO();
		
		for (PostReceiverVO data : receiverList) {
			smsDataVO.setUserId(data.getId());
			smsDataVO.setCellPhone(data.getCellPhone());
			smsService.send(smsDataVO, smsType);
		}
		
		return new RestResultVO<PostSendVO>(ResCode.CD2000.getCode(), ResCode.CD2000.getMessage());
	}
	
	
	
}
