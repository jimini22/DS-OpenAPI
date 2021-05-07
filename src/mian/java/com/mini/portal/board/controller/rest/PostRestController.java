package com.mini.portal.board.controller.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mini.portal.board.model.PostReceiverVO;
import com.mini.portal.board.model.PostResponseVO;
import com.mini.portal.board.model.PostSearchVO;
import com.mini.portal.board.model.PostSendVO;
import com.mini.portal.board.model.PostVO;
import com.mini.portal.board.service.PostService;
import com.mini.portal.comm.constants.AuthoritiesConstants;
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
import com.mini.portal.comm.utils.SecurityUserUtils;
import com.mini.portal.comm.utils.StringUtil;
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
	 * @description : 게시물 전체 조회
	 * @param svo
	 * @param boardType
	 * @param activeUser
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/{boardType}/list")
	public RestResultVO<PostResponseVO> getPostList ( @Valid PostSearchVO svo,
													@PathVariable(name="boardType", required = true) String boardType,
													@ActiveUser UserAuthVO activeUser ) throws Exception {
		
		svo.setBoardType(boardType);
		
		/* 사용자 권한에 따른 조회 조건 */
		activeUser = SecurityUserUtils.getCurrentUserInfo();
		if (boardType == "news") {
			if (activeUser != null) {
				if (activeUser.getAuthorities().contains(AuthoritiesConstants.MANAGER_PROVIDER) || activeUser.getAuthorities().contains(AuthoritiesConstants.USER_PROVIDER)) {
					/* 로그인한 사용자가 제공기관 >>> 본인의 기관으로 등록된 글만 조회 */
					svo.setOrganizationId(activeUser.getOrganizationId());
				}
			}
		} else if (boardType == "qna" || boardType == "question") {
			if (activeUser != null) {
				if (activeUser.getAuthorities().contains(AuthoritiesConstants.MANAGER_PROVIDER) || activeUser.getAuthorities().contains(AuthoritiesConstants.USER_PROVIDER)) {
					/* 로그인한 사용자가 제공기관 >>> 본인의 기관으로 등록된 글만 조회 */
					svo.setOrganizationId(activeUser.getOrganizationId());
				} else if (activeUser.getAuthorities().contains(AuthoritiesConstants.ADMIN)) {
					/* 로그인한 사용자가 admin >>> 전체 글 조회 가능 */
				} else {
					/* searchType(flag)이 myPost일 경우 내가 등록한 글만 조회 */
					if ("myPost".equals(svo.getSearchType())) {
						svo.setWriterId(activeUser.getId());
					}
				}
			}
		}
		
		/* 게시물 형식에 따른 조회 */
		PostResponseVO resVO = new PostResponseVO();
		if (boardType == "notice") {
			/* 게시물 전체 조회 (상단고정이 있는 경우) */
			resVO = postService.selectNoticeList(svo);
		} else {
			/* 게시물 전체 조회 */
			resVO = postService.selectPostList(svo);
		}
		log.info(":::::::::::::::: 게시글 목록 조회");
		
		RestResultVO<PostResponseVO> resultVO = new RestResultVO<PostResponseVO>(ResCode.CD2000.getCode(), ResCode.CD2000.getMessage(), resVO);
		return resultVO;
	}
	
	/**
	 * @description : 게시물 상세조회
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/detail/{id}")
	public RestResultVO<PostResponseVO> getPostDetail ( @PathVariable("id") Long id,
														@ActiveUser UserAuthVO activeUser ) throws Exception {
		
		PostVO vo = new PostVO();
		vo.setId(id);
		PostResponseVO resVO = postService.selectPostDetail(vo);
		log.info(":::::::::::::::: 게시글 상세정보 조회");
		
		/* 게시판 type에 따른 조건 */
		/* business */
		if (resVO.getPost().getBoardType().equals("business")) {
			/* 해당 권한을 가진 로그인 사용자의 경우에만 조회수 증가 */
			if (activeUser.getAuthorities().contains(AuthoritiesConstants.MANAGER_PROVIDER) || activeUser.getAuthorities().contains(AuthoritiesConstants.USER_PROVIDER)) {
				postService.hitPlus(id);
			}
		} else {
			postService.hitPlus(id);
		}
		
		RestResultVO<PostResponseVO> resultVO = new RestResultVO<PostResponseVO>(ResCode.CD2000.getCode(), ResCode.CD2000.getMessage(), resVO);
		return resultVO;
	}
	
	/**
	 * @description : 1:1문의글 상세조회 - 제공기관, 어드민
	 * @param vo
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Secured({"ROLE_ADMIN", "ROLE_MANAGER_PROVIDER", "ROLE_USER_PROVIDER"})
	@GetMapping(value = "/question/detail/{id}")
	public RestResultVO<PostResponseVO> getPostQuestionDetail ( @Valid PostVO vo,
																@PathVariable("id") Long id ) throws Exception {
		
		vo.setId(id);
		PostResponseVO resVO = postService.selectPostDetail(vo);
		resVO.getPost().setPassword(null);
		
		RestResultVO<PostResponseVO> resultVO = new RestResultVO<PostResponseVO>(ResCode.CD2000.getCode(), ResCode.CD2000.getMessage(), resVO);
		return resultVO;
	}
	
	/**
	 * @description : 1:1문의글 상세조회 - 비회원
	 * @param vo
	 * @param activeUser
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/question/detail")
	public RestResultVO<PostResponseVO> getPostQuestionUserDetail ( @Valid @RequestBody PostVO vo,
																	@ActiveUser UserAuthVO activeUser,
																	HttpServletRequest request ) throws Exception {
		
		HttpSession session = request.getSession();
		Long post_id = (Long) session.getAttribute("post_num");
		
		PostResponseVO resVO = postService.selectPostDetail(vo);
		resVO.getPost().setPassword(null);
		
		activeUser = SecurityUserUtils.getCurrentUserInfo();
		if (post_id == null || !resVO.getPost().getId().equals(post_id)) {
			if (activeUser != null && activeUser.getId().equals(resVO.getPost().getWriterId())) {
				postService.selectPostDetail(vo);
			} else {
				return new RestResultVO<PostResponseVO>(ResCode.CD9000.getCode(), ResCode.CD9000.getMessage());
			}
		}
		
		return new RestResultVO<PostResponseVO>(ResCode.CD2000.getCode(), ResCode.CD2000.getMessage());
	}	
	
	/**
	 * @description : 1:1문의글 상세조회 - 비밀번호 확인
	 * @param id
	 * @param password
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/check")
	public RestResultVO<?> getPostPasswordCheck ( @RequestParam(name = "id") Long id,
												  @RequestParam(name = "password") String password,
												  HttpServletRequest request ) throws Exception {
		
		PostVO vo = new PostVO();
		vo.setId(id);
		vo.setPassword(password);
		
		HttpSession session = request.getSession();
		
		PostResponseVO resVO = postService.selectPostDetail(vo);
		
		if (!resVO.getPost().getPassword().equals(vo.getPassword())) {
			// 게시글 비밀번호와 일치하지 않음
			return new RestResultVO<PostResponseVO>(ResCode.CD9020.getCode(), ResCode.CD9020.getMessage());
		}
		
		session.setAttribute("post_num", vo.getId());
		
		return new RestResultVO<>(ResCode.CD2000.getCode(), ResCode.CD2000.getMessage());
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
		
		if (SecurityUserUtils.isAuthenticated() == true) {
			vo.setWriterId(activeUser.getId());
			vo.setWriter(activeUser.getFullName());
			vo.setCreatedBy(activeUser.getLoginId());
		} else {
			vo.setWriterId(null);
		}
		
		vo.setContent(StringUtil.replaceXssFilter(vo.getContent()));
		
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
		vo.setContent(StringUtil.replaceXssFilter(vo.getContent()));
		
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
			smsType = SmsType.BOARD_QUESTION_AND_ANSWER_POST;
		} else if ("question".equals(type)) {
			smsType = SmsType.BOARD_ONE_TO_ONE_QUESTION_POST;
		} else if ("business".equals(type)) {
			smsType = SmsType.BOARD_BUSINESS_PROPOSAL_POST;
		} else if ("qnareply".equals(type)) {
			smsType = SmsType.BOARD_QUESTION_AND_ANSWER_REPLY;
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
	
	/**
	 * @description : admin에게 sms 발송
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/sms/{type}")
	public RestResultVO<PostSendVO> sendSmsToAdmin( @PathVariable(name="type", required = true) String type ) throws Exception {
		
		String smsType = "";
		if ("qna".equals(type)) {
			smsType = SmsType.BOARD_QUESTION_AND_ANSWER_POST;
		} else if ("question".equals(type)) {
			smsType = SmsType.BOARD_ONE_TO_ONE_QUESTION_POST;
		} else if ("business".equals(type)) {
			smsType = SmsType.BOARD_BUSINESS_PROPOSAL_POST;
		} else if ("qnareply".equals(type)) {
			smsType = SmsType.BOARD_QUESTION_AND_ANSWER_REPLY;
		} else if ("questionreply".equals(type)) {
			smsType = SmsType.BOARD_ONE_TO_ONE_QUESTION_REPLY;
		}
		
		PostSendVO sendVO = postService.sendMessageToAdmin();
		PostReceiverVO receiver = sendVO.getReceiver();
		
		if (receiver.equals(null)) {
			return new RestResultVO<PostSendVO>(ResCode.CD9041.getCode(), ResCode.CD9041.getMessage());
		}
		
		SmsDataVO smsDataVO = new SmsDataVO();
		smsDataVO.setUserId(receiver.getId());
		smsDataVO.setCellPhone(receiver.getCellPhone());
		smsService.send(smsDataVO, smsType);
		
		return new RestResultVO<PostSendVO>(ResCode.CD2000.getCode(), ResCode.CD2000.getMessage());
	}
	
	/**
	 * @description : user에게 sms 발송
	 * @param type
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/sms/{type}/user/{userId}")
	public RestResultVO<PostSendVO> sendSmsToUser( @PathVariable(name="type", required = true) String type,
													@PathVariable(name="userId", required = true) Long userId ) throws Exception {
		
		String smsType = "";
		if ("qnareply".equals(type)) {
			smsType = SmsType.BOARD_QUESTION_AND_ANSWER_REPLY;
		} else if ("questionreply".equals(type)) {
			smsType = SmsType.BOARD_ONE_TO_ONE_QUESTION_REPLY;
		}
		
		PostSendVO sendVO = postService.sendMessageToUser(userId);
		PostReceiverVO receiver = sendVO.getReceiver();
		
		SmsDataVO smsDataVO = new SmsDataVO();
		smsDataVO.setUserId(receiver.getId());
		smsDataVO.setCellPhone(receiver.getCellPhone());
		smsService.send(smsDataVO, smsType);
		
		return new RestResultVO<PostSendVO>(ResCode.CD2000.getCode(), ResCode.CD2000.getMessage());
	}
	
	/**
	 * @description : 게시글 글쓴이에게 sms 발송 (user id 없을 때)
	 * @param type
	 * @param boardId
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/sms/{type}/brd/{boardId}")
	public RestResultVO<PostSendVO> sendSmsToWriter( @PathVariable(name="type", required = true) String type,
													@PathVariable(name="boardId", required = true) Long boardId ) throws Exception {
		
		String smsType = "";
		if ("questionreply".equals(type)) {
			smsType = SmsType.BOARD_ONE_TO_ONE_QUESTION_REPLY;
		}
		
		PostVO vo = new PostVO();
		vo.setId(boardId);
		PostResponseVO resVO = postService.selectPostDetail(vo);
		log.info(":::::::::::::::: 게시글 상세조회 >>> {}", vo);
		
		PostReceiverVO receiver = new PostReceiverVO();
		SmsDataVO smsDataVO = new SmsDataVO();
		
		Long userId = resVO.getPost().getWriterId();
		String cellPhone = resVO.getPost().getCellPhone();
		
		if (userId == null) {
			smsDataVO.setCellPhone(cellPhone);
			log.info(":::::::::::::::: sms전송정보 ===== cell_phone : " + smsDataVO.getCellPhone());
		} else {
			PostSendVO sendVO = postService.sendMessageToUser(userId);
			receiver = sendVO.getReceiver();
			
			smsDataVO.setUserId(receiver.getId());
			smsDataVO.setCellPhone(cellPhone);
		}
		smsService.send(smsDataVO, smsType);
		
		return new RestResultVO<PostSendVO>(ResCode.CD2000.getCode(), ResCode.CD2000.getMessage());
	}
	
	
}
