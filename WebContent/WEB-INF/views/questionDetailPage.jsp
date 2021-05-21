<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/WEB-INF/views/common/attachFile.jsp"%>

<script type="text/javascript">
var postWriterId;	//글쓴이 id
var postWriter;		//글쓴이
var cellPhone;		//글쓴이 폰번호
var organization	//문의글 제공기관

$(document).ready(function() {
	
	getTable();
	
	listReply();
	
	var boardId = "${param.id}";
	BoardCommon.listAttach(null, boardId);
	
	$(".download").on('click', 'a', function(e) {
		var attachId = $(this).data("attach_id");
		BoardCommon.attachDownload(attachId);
	});
	
	$("#list").on('click', function() {
		window.location.href = "questions/list?" + $.param(getSearchParam());
	});
	
	$(document).on('click', '#rpl_create_btn', function(e) {
		e.preventDefault();
		
		var content = $('#rContent').val();
		var boardType = "questionreply";
		var data = { boardId : boardId,
					 content : content,
					 boardType : boardType,
					 writer : postWriter,
					 cellPhone : cellPhone };
		if (!content) {
			$(".error-text").show();
			return;
		}
		
		if (${fn:contains(user.authorities, 'ROLE_MANAGER_PROVIDER') || fn:contains(user.authorities, 'ROLE_USER_PROVIDER')}) {
			var asyncWrite = Ajax.doPost('/rest/reply/write', data, fnSuccess)
								 .catch(fnError);
			var asyncSend = Ajax.doPost('/rest/posts/sms/' + data.boardType + '/brd/' + data.boardId, fnSuccess)
								.done(function(resData) {})
								.catch(fnError);
			$.when(asyncWrite, asyncSend).done(function (res1, res2) {
				listReply();
			});
		} else {
			if (organization) {
				var asyncWrite = Ajax.doPost('/rest/reply/write', data, fnSuccess)
									 .catch(fnError);
				var asyncSend = Ajax.doPost('/rest/posts/sms/' + data.boardType + '/' + data.organization, fnSuccess)
									.done(function(resData) {})
									.catch(fnError);
				$.when(asyncWrite, asyncSend).done(function (res1, res2) {
					listReply();
				});
			} else {
				var asyncWrite = Ajax.doPost('/rest/reply/write', data, fnSuccess)
				 					 .catch(fnError);
				var asyncSend = Ajax.doPost('/rest/posts/sms/' + data.boardType, fnSuccess)
									.done(function(resData) {})
									.catch(fnError);
				$.when(asyncWrite, asyncSend).done(function (res1, res2) {
					listReply();
				});
			}
		}
	});
	$(".error-text").hide();
});

function getTable(id) {
	
	var id = "${param.id}";
	
	if (${fn:contains(user.authorities, 'ROLE_ADMIN') || fn:contains(user.authorities, 'ROLE_MANAGER_PROVIDER') || fn:contains(user.authorities, 'ROLE_USER_PROVIDER')}) {
		var url = "/rest/board/detail/" + id;
		
		Ajax.doGet(url).then(function(resData) {
			showTable(resData.responseData.data.question);
		}).catch(function(resData) {
			Alert.show("게시물 상세조회 중 오류가 발생했습니다.\n다시 시도하여 주십시오.", "오류");
		});
	} else {
		var url = "/rest/board/detail";
	}
	
	
}



</script>


<div id="container" class="container">
	<section>
		<div class="contents">
			<div class="title"><h1 class="title-h1">1:1 문의</h1></div>	
			<div class="contents-item">
				<div class="table-form">
					<div class="desc-form"><span class="required">필수항목</span></div>
					<form id="writeform" method="post">
						<input name="boardType" id="boardType" value="question" type="hidden">
						<table>
							<colgroup>
								<col width="270px">
								<col width="auto">
							</colgroup>
							<tbody>
								<tr>
									<th><span class="label required">문의할 기관</span></th>
									<td>
										<div class="radio-group" id="organization"></div>
										<p class="error-text text1" style="display: none;">문의할 기관을 선택하세요</p>
									</td>
								</tr>
								<tr>
									<th><label for="label-title" class="required">제목</label></th>
									<td>
										<span class="input"><input type="text" name="title" id="label-title" placeholder="제목 입력"></span>
										<p class="error-text text2" style="display: none;">문의 제목을 입력하세요</p>
									</td>
								</tr>
								<tr>
									<th><label for="label-pw1" class="required">비밀번호</label></th>
									<td>
										<span class="input"><input type="password" name="passwd" id="label-pw1" placeholder="비밀번호 입력"></span>
										<p class="error-text text3" style="display: none;">비밀번호를 입력하세요</p>
										<p class="error-text text3-1" style="display: none;">비밀번호는 숫자로 입력하세요</p>
										<p class="error-text text3-2" style="display: none;">비밀번호는 4~20자 이내로 입력하세요</p>
									</td>
								</tr>
								<tr>
									<th><label for="label-content" class="required">내용</label></th>
									<td>
										<div id="summernote-edit" class="terms terms-content"></div>
										<p class="error-text text4" style="display: none;">문의 내용을 입력하세요</p>
									</td>
								</tr>
								<tr class="attach-file">
									<th>
										<div class="label">첨부파일
											<div class="tooltip">
												<a href="javascript:void(0);" class="tooltip-button" title="hint">?</a>
												<div class="tooltip-layer text-list-dash">
													<ul>
														<li>첨부파일은 총 10MB 이내로 등록하여 주십시오.</li>
														<li>문서 파일과 이미지 파일만 등록 가능합니다.</li>
														<li>doc, xls, ppt, hwp, pdf, gif, jpg, png</li>
													</ul>
													<a href="javascript:void(0);" class="tooltip-close-button">close</a>												
												</div>
											</div>
										</div>
									</th>
									<td>
										<!-- form 태그를 tr사이에 두라는데 (구글).....이거 수정해도 문제없는가?? -->
										<form id="form_upload" method="post">
											<!-- input type hidden 값은 td 안에 넣어야 테이블이 깨지지 않음 -->
											<input name="attachType" id="attachType" type="hidden" value="question">
											<span class="attach-file-button">
												<label for="attach-file">찾아보기</label>
												<input type="file" id="attach-file">
											</span>
											<div class="table-attach-file">
												<table>
													<colgroup>
														<col width="">
														<col width="">
														<col width="">
													</colgroup>
													<thead>
														<tr>
															<th>파일명</th>
															<th>파일크기</th>
															<th>삭제</th>
														</tr>
													</thead>
													<tbody id="file-list">
														<tr class="file-drop-area">
															<td colspan="3">
																<div class="file-drop no-data" id="file-drop">
																	<p>파일을 드래그 하세요</p>
																</div>
															</td>
														</tr>
													</tbody>
												</table>
											</div>
										</form>
									</td>
								</tr>
								<tr>
									<th><label for="label-name" class="required">이름</label></th>
									<td>
										<span class="input"><input type="text" name="writer" id="label-name" placeholder="이름 입력" maxlength="20"></span>
										<p class="error-text text5" style="display:none;">이름을 입력하세요</p>
										<p class="error-text text5-1" style="display:none;">이름을 확인하세요</p>
									</td>
								</tr>
								<tr class="cell-phone">
									<th><label for="label-phone" class="required">휴대폰번호</label></th>
									<td>
										<span class="select">
											<select id="label-phone" name="cellPhone" title="휴대폰 앞자리 선택">
												<option value="">선택</option>
												<option value="010">010</option>
												<option value="011">011</option>
												<option value="017">017</option>
											</select>
										</span>
										<span class="dash">-</span>
										<span class="input">
											<input type="text" name="cellPhone" id="middle" title="휴대폰 가운데자리 입력">
										</span>
										<span class="dash">-</span>
										<span class="input">
											<input type="text" name="cellPhone" id="back" title="휴대폰 뒷자리 입력">
										</span>
										<p class="error-text text6" style="display:none;">휴대폰 번호를 입력하세요</p>
										<p class="error-text text6-1" style="display:none;">휴대폰 번호를 확인하세요</p>
									</td>
								</tr>
								<tr class="email">
									<th><label for="label-email">이메일</label></th>
									<td>
										<span class="input">
											<input type="email" name="email" id="label-email" placeholder="이메일 아이디" title="이메일 아이디 입력">
										</span>
										<span class="dash">@</span>
										<span class="input">
											<input type="email" name="email" id="domain" placeholder="도메인" title="도메인 입력">
										</span>
										<span class="select">
											<select name="email" id="domainSelect" title="도메인 선택">
												<option value=""></option>
												<option value="gmail.com">gmail.com</option>
												<option value="naver.com">naver.com</option>
												<option value="hanmail.net">hanmail.net</option>
											</select>
										</span>
										<p class="error-text text7" style="display:none;">이메일을 입력하세요</p>
										<p class="error-text text7-1" style="display:none;">이메일을 양식에 맞게 입력해주세요</p>
									</td>
								</tr>
							</tbody>
						</table>
					</form>
				</div>
			</div>
			<div class="contents-item">
				<div class="term">
					<div class="sub-title desc-from"><h2 class="title-h2">약관동의</h2></div>
					<div class="check-all-group term-agree default-accordion" data-accordion="checkbox" data-checkbox="check-all">
						<div class="check-list" data-id="term-agree">
							<dl class="accordion-item">
								<dt class="accordion-title">
									<div class="checkbox-item">
										<input type="checkbox" name="term-agree" id="term-agree">
										<label for="term-agree">개인정보 이용 및 제공 동의 (필수)</label>
										<p class="error-text text8" style="display:none;">개인정보 이용 및 제공에 동의해주세요</p>
									</div>
									<span class="arrow-button">open</span>
								</dt>
								<dd class="accordion-panel">
									<div class="terms-content" id="term-agree-text"></div>
								</dd>
							</dl>
						</div>
					</div>
				</div>
			</div>
			<div class="contents-item"></div>			
			<div class="bottom-area center">
				<a href="javascript:void(0);" class="button secondary" id="list">목록</a>
				<a href="javascript:void(0);" class="button primary" id="upload_btn">목록</a>
			</div>
		</div>
	</section>
</div>
