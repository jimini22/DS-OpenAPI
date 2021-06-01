<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/WEB-INF/views/common/editor.jsp"%>		<!-- 파일 없음 -->
<%@ include file="/WEB-INF/views/common/input.jsp"%>		<!-- 파일 없음 -->
<%@ include file="/WEB-INF/views/common/attachFile.jsp"%>

<script type="text/javascript">

var $fileList;
var passwordCheckRegExp	= RegExp(/^[0-9]*$/);
var cellPhoneRegExp		= RegExp(/^\d{10,11}$/);
var userNameCheckRegExp	= RegExp(/^[-가-힣A-Za-z0-9._]{2,20}$/);
var emailFirstRegExp	= RegExp(/^[-A-Za-z0-9._]*$/);
var emailSecondRegExp	= RegExp(/^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/);

var organizationCheck = false;
var titleCheck = false;
var passwordCheck = false;
var contentCheck = false;
var nameCheck = false;
var cellPhoneCheck = false;
var emailCheck = false;
var termCheck = false;

var cellPhoneCheckTest = false;
var emailCheckTest = false;

var userInfo = ${userInfo};

$(document).ready(function() {

	$fileList = $("#file-list");
	
	<%-- 제공기관 목록 --%>
	getOrganizationList();
	
	<%-- editor setting --%>
	var opt = {height: 300, count: false, disableResizeEditor: false, placeholder: "", max: 500};
	EditorUtil.create($("#summernote-edit"), "", opt);
	
	<%-- 업로드할 목록 보여주기 (첨부파일 업로드 > [찾아보기]버튼) --%>
	$(document).on('change', '#attach-file', function(e) {
		var files = e.target.files;
		BoardCommon.setAttachInputFile(files, $fileList);
	});
	
	<%-- 업로드할 목록 보여주기 (첨부파일 업로드 > drag&drop) --%>
	BoardCommon.setAttachDropFile($fileList);
	
	<%-- 업로드할 목록 보여주기 (첨부파일 업로드 > 업로드할 파일 삭제) --%>
	BoardCommon.attachFileRemove($fileList);
	
	<%-- 약관동의 --%>
	getContent("privacy-policy", "term-agree-text");
	
	<%-- 휴대폰 번호 입력제한 --%>
	InputUtil.set("middle", "number", {commify: false, minlen: 3, maxlen: 4, fn: function(status) {
		if (status === "ok") {}
		else if (status === "num") {}
		else if (status === "minlen") {}
		else if (status === "maxlen") {}
	}});
	InputUtil.set("back", "number", {commify: false, minlen: 4, maxlen: 4, fn: function(status) {
		if (status === "ok") {}
		else if (status === "num") {}
		else if (status === "minlen") {}
		else if (status === "maxlen") {}
	}});
	
	<%-- 기관 radio 선택 check --%>
	$(document).on('click', 'input:radio[name="organizationId"]', function() {
		organizationValid();
	});
	<%-- 제목 check --%>
	$("#label-title").bind("blur", function() {
		titleValid();
	});
	<%-- 비밀번호 check --%>
	$("#label-pw1").bind("blur", function() {
		passwordValid();
	});
	<%-- 내용 check --%>
	$("#summernote-edit").next().find(".note-editable").bind("blur", function() {
		contentValid();
	});
	<%-- 이름 check --%>
	$("#label-name").bind("blur", function() {
		nameValid();
	});
	<%-- 휴대폰번호 check --%>
	$("#label-phone").on("blur", function() {
		cellPhoneCheckTest = true;
		cellPhoneValid();
	});
	$("#middle, #back").bind("blur", function() {
		cellPhoneCheckTest = true;
		cellPhoneValid();
	});
	<%-- 이메일 check --%>
	$("#label-email").bind("blur", function() {
		emailCheckTest = true;
		emailValid();
	});
	$("#domain").bind("blur keyup change", function() {
		emailCheckTest = true;
		$("#domainSelect").val("").prop("selected", true);
	});
	$("#domainSelect").bind("blur keyup change", function() {
		emailCheckTest = true;
		$("#domain").val(this.value);
	});
	
	<%-- 로그인 사용자의 경우 값 미리 세팅하기 --%>
	<c:choose>
		<c:when test="${isAuthenticated == false}">
		</c:when>
		<c:otherwise>
			$("#label-name").val(userInfo.fullName);
			$("#label-phone").val(userInfo.cellPhone.split('-')[0]);
			$("#middle").val(userInfo.cellPhone.split('-')[1]);
			$("#back").val(userInfo.cellPhone.split('-')[2]);
			$("#label-email").val(userInfo.email.split('@')[0]);
			$("#domain").val(userInfo.email.split('@')[1]);
		</c:otherwise>
	</c:choose>
	
	<%-- [등록]버튼 클릭시 >> 1:1 문의글 등록 --%>
	$(document).on('click', '#upload_btn', function(e) {
		e.preventDefault();
		postWrite();
	});
	$(".error-text").hide();
	
	<%-- 목록 버튼 --%>
	$("#list").on('click', function() {
		window.location.href = "/post/list?" + $.param(getSearchParam());
	});
	
});

function organizationValid() {
	if (!$('input:radio[name="organizationId"]:checked').val()) {
		$(".text1").show();
		organizationCheck = false;
	} else {
		$(".text1").hide();
		organizationCheck = true;
	}
}

function titleValid() {
	if (!$("#label-title").val()) {
		$(".text2").show();
		titleCheck = false;
	} else {
		$(".text2").hide();
		titleCheck = true;
	}
}

function passwordValid() {
	var password = $("#label-pw1").val();
	$("#label-pw1").val($.trim(password));
	if (!$("#label-pw1").val()) {
		$(".text3").show();
		passwordCheck = false;
	} else {
		$(".text3").hide();
		$(".text3-1").hide();
		if (!passwordCheckRegExp.test($("#label-pw1").val())) {
			$(".text3-1").show();
			passwordCheck = false;
		} else {
			$(".text3-2").hide();
			passwordCheck = true;
		}
		$(".text3-2").hide();
		if ($("#label-pw1").val().length < 4 || $("#label-pw1").val().length > 20) {
			$(".text3-2").show();
			passwordCheck = false;
		} else {
			$(".text3-2").hide();
			passwordCheck = true;
		}
	}
}

function contentValid() {
	if (!EditorUtil.getValue($("#summernote-edit"))) {
		$(".text4").show();
		contentCheck = false;
	} else {
		$(".text4").hide();
		contentCheck = true;
	}
}

function nameValid() {
	if (!$("#label-name").val()) {
		$(".text5").show();
		nameCheck = false;
	} else {
		$(".text5").hide();
		if (!userNameCheckRegExp.test($("#label-name").val())) {
			$(".text5-1").show();
			nameCheck = false;
		} else {
			$(".text5-1").hide();
			nameCheck = true;
		}
	}
}

function cellPhoneValid() {
	var cellPhone = $("#label-phone option:selected").val() + $("#middle").val() + $("#back").val();
	var phone1 = $("#label-phone option:selected").val();
	var phone2 = $("#middle").val();
	var phone3 = $("#back").val();
	if (!phone1 || !phone2 || !phone3) {
		$(".text6").show();
		cellPhoneCheck = false;
	} else {
		$(".text6").hide();
		if (!cellPhoneRegExp.test(cellPhone)) {
			$(".text6-1").show();
			cellPhoneCheck = false;
		} else {
			$(".text6-1").hide();
			cellPhoneCheck = true;
		}
	}
}

function emailValid() {
	var email1 = $("#label-email").val();
	var email2 = $("#domain").val();
	$(".text7-1").hide();
	emailCheck = true;
	if (email1 || email2) {
		if (emailFirstRegExp.test(email1) == false || emailSecondRegExp.test(email2) == false) {
			$(".text7-1").show();
			emailCheck = false;
		} else {
			$(".text7-1").hide();
			emailCheck = true;
		}
	}
}

function termValid() {
	if ($('input:checkbox[id="term-agree"]').is(":checked")) {
		$(".text8").hide();
		termCheck = true;
	} else {
		$(".text8").show();
		$('input:checkbox[id="term-agree"]').focus();
		termCheck = false;
	}
}

function validationCheck() {
	<c:choose>
		<c:when test="${isAuthenticated == false}">
			organizationValid();
			titleValid();
			passwordValid();
			contentValid();
			nameValid();
			cellPhoneValid();
			emailValid();
			termValid();
		</c:when>
		<c:otherwise>
			organizationValid();
			titleValid();
			passwordValid();
			contentValid();
			nameValid();
			if (cellPhoneCheckTest) {
				cellPhoneValid();
			} else {
				cellPhoneCheck = true;
			}
			if (emailCheckTest) {
				emailValid();
			} else {
				emailCheck = true;
			}
			termValid();
		</c:otherwise>
	</c:choose>
	
	if (organizationCheck && titleCheck && passwordCheck && contentCheck && nameCheck && cellPhoneCheck && emailCheck && termCheck) {
		return true;
	}
	return false;
}

<%-- 새 글 등록하기 --%>
function postWrite() {
	
	if (!validationCheck()) {
		return;
	} else {
		var json = FormUtil.toJson("#writeform");
		json.organizationId = $('input:radio[name="organizationId"]:checked').val();
		json.content = EditorUtil.getValue($("summernote-edit"));
		json.cellPhone = $("#label-phone option:selected").val() + $("#middle").val() + $("#back").val();
		json.phone1 = $("#label-phone option:selected").val();
		json.phone2 = $("#middle").val();
		json.phone3 = $("#back").val();
		json.email = $("#label-email").val() + "@" + $("#domain").val();
		json.email1 = $("#label-email").val();
		json.email2 = $("#domain").val();
		if (json.organizationId == "00") {
			json.organizationId = "";
		}
		if (userInfo) {
			if (!cellPhoneCheckTest && cellPhoneCheck && json.cellPhone == userInfo.cellPhone.replaceAll('-', '')) {
				json.cellPhone = '${cellPhone}';
			}
			if (!emailCheckTest && emailCheck && json.email == userInfo.email) {
				json.email = '${email}';
			}
		}
		
		Ajax.doPost('/rest/posts/write', json).then(function(resData) {
			if (resData.responseData.code == 2000) {
				var postId = resData.responseData.data;
				var fileData = {"attachType" : "board", "postId" : postId};
				BoardCommon.addAttachFileList($fileList, postId, json, fileData);
			} else {
				Alert.show("1:1문의 등록 중 오류가 발생했습니다.\n다시 시도하여 주십시오.", "오류");
			}
		}).catch(function(resData) {
			Alert.show("1:1문의 등록 중 오류가 발생했습니다.\n다시 시도하여 주십시오.", "오류");
		});
	}
}
<%-- //새 글 등록하기 --%>

<%-- sms 메시지 전송 --%>
function smsSend(json, fileResult) {
	Ajax.doPost('/rest/posts/sms/' + json.boardType + '/' + json.organizationId).then(function(resData) {
		if (fileResult == false) {
			Alert.show("파일 등록을 실패하였습니다.", function() {
				location.href = "/post/write/finished";
			});
		}
		if (resData.responseData.code != 2000) {
			Alert.show(resData.responseData.message, function() {
				location.href = "/post/write/finished";
			});
		} else {
			location.href = "/post/write/finished";
		}
	}).catch(function(resData) {
		Alert.show("문자 전송 요청 실패", function() {
			location.href = "/post/write/finished";
		});
	});
}
<%-- //sms 메시지 전송 --%>

<%-- 제공기관 목록 --%>
function getOrganizationList() {
	var url = "/rest/organization/providers";
	Ajax.setLoadingDisable("continue");
	Ajax.doGet(url).then(function(resData) {
		showOrganizationList(resData.responseData.data);
	}).catch(function(resData) {
		Alert.show("제공기관 목록 조회 중 오류가 발생했습니다.\n다시 시도하여 주십시오.", "오류");
	});
}

function showOrganizationList(data) {
	$("#organization").html('');
	
	var html = [];
	for (n in data.list) {
		var org = data.list[n];
		html.push('<span class="radio-item">');
		html.push(	'<input type="radio" name="organizationId" id="organization' + org.organizationId + '" value="' + org.organizationId + '">');
		html.push(	'<label for="organization' + org.organizationId + '">' + (org.organizationName||"") + '</label>');
		html.push('</span>');
	}
	$("#organization").append(html.join(''));
	
	var etc = [];
	etc.push('<span class="radio-item">');
	etc.push(	'<input type="radio" name="organizationId" id="organization00" value="00">');
	etc.push(	'<label for="organization00">기타</label>');
	etc.push('</span>');
	$("#organization").append(etc.join(''));
}
<%-- //제공기관 목록 --%>

<%-- 약관동의 --%>
function getContent(kind, returnElement) {
	var url = "/rest/contents/using/" + kind;
	Ajax.doGet(url, function(resData) {
		if (resData.responseData.code == 2000) {
			var jtxt = resData.responseData;
			if (jtxt.data != null) {
				$("#" + returnElement).html(jtxt.data.contentsBody);
			}
		} else {
			Alert.show(resData.responseData.message);
		}
	});
}
<%-- //약관동의 --%>

<%-- parameter setting --%>
function getSearchParam() {
	var paramInfo = {};
	paramInfo.pageNo = App.urlParameters.pageNo;
	paramInfo.keyword = App.urlParameters.keyword;
	paramInfo.organizationId = App.urlParameters.organizationId;
	paramInfo.replyState = App.urlParameters.replyState;
	paramInfo.searchType = App.urlParameters.searchType;
	paramInfo.writer = App.urlParameters.writer;
	return paramInfo;
}
<%-- //parameter setting --%>

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
