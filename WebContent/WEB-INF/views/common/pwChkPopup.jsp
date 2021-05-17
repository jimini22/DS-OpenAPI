<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="popup-area">
	<div class="popup-layer medium" data-id="popup-pwChk">
		<div class="popup-wrap">
			<div class="popup-header">
				<h1>비밀번호 확인</h1>
			</div>
			<div class="popup-container">
				<div class="input" data-id="input-active">
					<input type="password" class="in-password" id="password" placeholder="비밀번호 입력">
					<p class="input-desc input-error errortext" style="display: none;"></p>
				</div>
				<div class="btn-area">
					<a href="javascript:void(0);" class="button small secondary" id="close_btn">닫기</a>
					<a href="javascript:void(0);" class="button small primary" id="check_btn">확인</a>
				</div>
			</div>
			<a href="javascript:void(0);" class="popup-close">닫기</a>
		</div>
	</div>
</div>

<script type="text/javascript">
var passwordRegExp = RegExp(/^\d$/);

window.passwordCheckPopup = (function (popup) {
	'use strict';
	
	var $popup = null;
	var callbackFunc = null;
	var postId = null;	// 게시글 id
	var postPw = null;	// 게시글 pw
	var inPw = null;	// 입력한 pw
	
	$(function(){
		$popup = $('[data-id="popup-pwChk"]');
		$popup.find("#check_btn").on('click', popup.detail);
		$("#password").on('keyup', function(e){
			if(e.keyCode == 13) {
				$("#check_btn").click();
			}
		});
		$popup.find("#close_btn").on('click', popup.close);
	});
	
	<%-- 팝업 초기화 --%>
	popup.clear = function() {
		callbackFunc = null;
		$(".input").removeClass("input-error");
		$(".errortext").text("비밀번호를 입력하세요");
		$(".errortext").hide();
	}
	
	<%-- 팝업 진입 --%>
	popup.show = function(id, callback) {
		callbackFunc = callback;
		postId = id;
		$popup.find("#password").val("");
		popup_open($popup);
	}
	
	<%-- 팝업 닫기 --%>
	popup.close = function() {
		callbackFunc();
		popup_close($popup);
	}
	
	<%-- 실행 --%>
	popup.detail = function() {
		
		inPw = $popup.find("#password").val();
		if (inPw == "") {
			$(".input").addClass("input-error");
			$(".errortext").text("비밀번호를 입력하세요");
			$(".errortext").show();
			return false;
		}
		
		var formData = new FormData();
		formData.append("id", postId);
		formData.append("password", inPw);
		
		var url = "/rest/posts/check";
		Ajax.doFormData(url, formData, null).then(function(resData){
			if (resData.responseData.code == 2000) {
				location.href = "/post/detail?" + $.param(getSearchParam(pageNumber)) + "&id=" + postId;
			} else if (resData.responseData.code == 9020) {
				$(".input").addClass("input-error");
				$(".errortext").text("비밀번호가 일치하지 않습니다");
				$(".errortext").show();
			}
		}).catch(function(resData){
			Alert.show("비밀번호 확인 실패");
		});
		
	}
	
	return popup;
})({});
</script>