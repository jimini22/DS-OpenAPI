<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/WEB-INF/views/common/pwChkPopup.jsp"%>

<script type="text/javascript">
var pageNumber = 1;
var searchType = App.urlParameters.searchType;

$(document).ready(function(){
	
	getOrganizationList().then(function(resolve){
		showOrganizationList(resolve);
	}).then(function(){
		settingParameter();
	}).then(function(){
		getQuestionList(App.urlParameters.pageNo);
	}).catch(function(resData){
		Alert.show("오류가 발생하였습니다.\n다시 시도하여 주십시오.", "오류");
	});
	
	<%-- 검색 버튼 클릭시 --%>
	$(document).on('click', '#search-btn', function(e){
		e.preventDefault();
		getQuestionList();
	});
	$('#keyword, #writer').keydown(function(key){
		if (key.keyCode == 13) {
			getQuestionList();
		}
	});
	
	<%-- 글쓰기 버튼 클릭시 --%>
	$(document).on('click', '#create-btn', function(e){
		e.preventDefault();
		location.href = "/post/write?" + $.param(getSearchParam(pageNumber));
	});
	
	<%-- 제목 클릭시 >> 게시글 상세보기 화면 이동 --%>
	$(document).on('click', '#listAll .board-title a', function(e){
		e.preventDefault();
		var id = $(this).data('item_id');
		var writerId = $(this).data('item_writer');
		var logWriterId = "${user.id}";
		
		if (${fn:contains(user.authorities, 'ROLE_ADMIN') || fn:contains(user.authorities, 'ROLE_MANAGER_PROVIDER') || fn:contains(user.authorities, 'ROLE_USER_PROVIDER')}) {
			location.href = "/post/detail?" + $.param(getSearchParam(pageNumber)) + "&id=" + id;
		} else if (${isAuthenticated == true} && logWriterId == writerId) {
			location.href = "/post/detail?" + $.param(getSearchParam(pageNumber)) + "&id=" + id;
		} else {
			postPopupFunction.pwChk(id);
		}
	});
	
});

<%-- 1:1문의 전체보기 --%>
function getQuestionList(pageNo) {
	
	var url = "/rest/posts/question/list?"
	url += $.param(getSearchParam(pageNo));
	
	Ajax.doGet(url).then(function(resData){
		showQuestionList(resData.responseData.data);
	}).catch(furnction(resData){
		Alert.show("1:1문의 목록 조회 중 오류가 발생하였습니다.\n다시 시도하여 주십시요.", "오류");
	});
	
}

function showQuestionList(data) {
	
	$("#listAll").html("");
	
	<%-- 리스트출력 (게시글 없을 때) --%>
	if(data.questionList.length == 0) {
		var html = '<tr><td colspan="6"><div class="no-data"><p>조회내역이 없습니다.</p></div></td></tr>';
		$("#listAll").append(html);
	}
	
	<%-- 리스트출력 (게시글 있을 때) --%>
	var html = [];
	for (n in data.questionlist) {
		var item = data.questionList[n];
		var page = data.pageInfo;
		var dateStr = item.createdDate;
		var yearMonthDay = dateStr.split('-')[0] + '.' + dateStr.split('-')[1] + '.' + (dateStr.split('-')[2]).substring(0, 2);
		var commentCnt = item.commentTotCnt > 0 ? "&nbsp&nbsp["+item.commentTotCnt+"]" : "";
		var iconCss = !item.attachFileTotCnt ? "" : "ico-file";
		var repState = "";
		
		if (item.replyState == "n") {
			repState = "미등록";
		} else if (item.replyState == "y") {
			repState = "등록";
		}
		
		html.push('<tr>');
		html.push(	'<td class="no">' + (page.totalCnt=(page.currentPage*page.pageSize)+(page.pageSize-n)||"") + '</td>');
		html.push(	'<td class="board-title"><a href="#" data-item_id="' + (item.id||"") + '" data-item_writer="' + (item.writerId||"") + '" class="' + iconCss + '">' + StringUtils.escapeHtml(item.title||"제목없음") + replyCnt + '</a></td>');
		html.push(	'<td class="org">' + (item.orgName||"기타") + '</td>');
		html.push(	'<td class="state">' + repState + '</td>');
		html.push(	'<td class="name">' + (item.writer||"") + '</td>');
		html.push(	'<td class="date">' + yearMonthDay + '</td>');
		html.push('</tr>');
	}
	$("#listAll").append(html.join(''));
	
	<%-- 리스트 총 갯수 출력 --%>
	$("#totCnt").text(data.pageInfo.totalCnt + "개");
	
	<%-- 페이징 정보 --%>
	PaginationUtil.create("pagination", 10, data.pageInfo, getQuestionList);
}
<%-- //1:1문의 전체보기 --%>

<%-- 제공기관 목록 --%>
function getOrganizationList() {
	return new Promise(function(resolve, reject) {
		var url = "/rest/organization/providers";
		Ajax.doGet(url).then(furnction(resData) {
			resolve(resData);
		}).catch(function(resData) {
			Alert.show("제공기관 목록 조회 중 오류가 발생하였습니다.\n다시 시도하여 주십시오.", "오류");
		});
	});
}

function showOrganizationList(data) {
	var data = data.responseData.data;
	$("#orgId").html('<option value="">전체</option>');
	var html = [];
	for (n in data.list) {
		var org = data.list[n];
		html.push('<option value="' + (org.organizationId||"") + '">' + (org.organizationName||"") + '</option>');
	}
	$("#orgId").append(html);
	
	var etc = [];
	etc.push('<option value="0">기타</option>');
	$("#orgId").append(etc);
}
<%-- //제공기관 목록 --%>

<%-- 비밀번호 확인 팝업 --%>
var postPopupFunction = (function(postPopupFunction) {
	postPopupFunction.pwChk = function (id) {
		passwordCheckPopup.clear();
		passwordCheckPopup.show(id, function() {
			passwordCheckPopup.clear();
		});
	}
	return postPopupFunction;
})({});
<%-- //비밀번호 확인 팝업 --%>

<%-- url parameter setting --%>
function settingParameter() {
	$("#keyword").val(App.urlParameters.keyword);
	$("#orgId").val(App.urlParameters.organizationId).prop("selected", true);
	$("#replyState").val(App.urlParameters.replyState).prop("selected", true);
	$("#writer").val(App.urlParameters.writer);
}

function getSearchParam(page) {
	var paramInfo = {};
	paramInfo.pageNo = page||1;
	paramInfo.keyword = $("#keyword").val().trim();
	paramInfo.organizationId = $("#orgId option:selected").val();
	paramInfo.replyState = $("#replyState option:selected").val();
	paramInfo.searchType = searchType;
	paramInfo.writer = $("#writer").val().trim();
	pageNumber = paramInfo.pageNo;
	return paramInfo;
}
<%-- //url parameter setting --%>

<%-- fnSuccess, fnError --%>
function fnSuccess(res) {
	if (res.responseData.code !== 2000) {Alert.show(res.responseData.message);}
}
function fnError(res) {
	throw Error(error.responseData);
}
<%-- //fnSuccess, fnError --%>
</script>


<div id="container" class="container">
	<section>
		<div class="contents">
			<div class="title"><h1 class="title-h1">1:1문의</h1></div>		
			<div class="contents-item">
				<div class="search-area type2">
					<div class="space-between">
						<div class="search-item">
							<span>제목/내용</span>
							<div class="input" data-id="input-active">
								<input type="text" id="keyword" name="keyword">
								<a href="#" class="input-clear" data-id="input-clear">지우기</a>
							</div>
						</div>
						<c:choose>
							<c:when test="${fn:contains(user.authorities, 'ROLE_MANAGER_PROVIDER') || fn:contains(user.authorities, 'ROLE_USER_PROVIDER')}">
							</c:when>
							<c:otherwise>
								<div class="search-item category">
									<span>문의기관</span>
									<div class="select">
										<select id="orgId"></select>
									</div>
								</div>
							</c:otherwise>
						</c:choose>
						<div class="search-item category">
							<span>답변여부</span>
							<div class="select">
								<select id="replyState">
									<option value="">선택</option>
									<option value="n">미등록</option>
									<option value="y">등록</option>
								</select>
							</div>
						</div>
						<div class="search-item">
							<span>작성자</span>
							<div class="input" data-id="input-active">
								<input type="text" id="writer" name="writer">
								<a href="#" class="input-clear" data-id="input-clear">지우기</a>
							</div>
						</div>
						<a href="javascript:void(0);" class="button secondary medium" id="search-btn">검색</a>
					</div>
				</div>
				<p class="count">총 <b id="totCnt"></b>의 게시물</p>			
				<div class="table-board center">
					<table>
						<colgroup>
							<col width="104px"><col width="auto"><col width="140px"><col width="140px"><col width="130px"><col width="150px">
						</colgroup>
						<thead>
							<tr>
								<th>No</th><th>제목</th><th>문의기관</th><th>답변여부</th><th>작성자</th><th>작성일</th>
							</tr>
						</thead>
						<tbody id="listAll">
						</tbody>
					</table>
				</div>
				<div class="pagination-area">
					<div class="pagination" id="pagination"></div>
					<c:choose>
						<c:when test="${fn:contains(user.authorities, 'ROLE_ADMIN') || fn:contains(user.authorities, 'ROLE_MANAGER_PROVIDER') || fn:contains(user.authorities, 'ROLE_USER_PROVIDER')}">
						</c:when>
						<c:otherwise>
							<a href="javascript:void(0);" class="button primary" id="create-btn">문의하기</a>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>	
	</section>
</div>