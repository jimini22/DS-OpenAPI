<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%--
	Alert 창 기능을 대신하는 팝업 창
--%>

<div class="popup-layer small" data-id="popup-common-alert">
	<div class="popup-wrap">
		<div class="popup-header">
			<h1><b id="alertType"></b></h1>
		</div>
		<div class="popup-container center">
			<p id="alertMessage">%메시지가 표시되는 항목%</p>
			<div class="btn-area">
				<a href="javascript:Alert.close();" class="button small primary">확인</a>
			</div>
		</div>
		<a href="javascript:Alert.close();" class="popup-close">닫기</a>
	</div>
</div>

<script type="text/javascript">
window.Alert = (function(popup) {
	'use strict';
	
	var $popup = null;
	var callback = null;
	$(function() {
		$popup = $('.popup-layer[data-id="popup-common-alert"]');
	});
	
	popup.show = function(message, title, fn) {
		message = message || "서비스 요청 처리 중 오류가 발생했습니다.";
		callback = null;
		var len = argument.length;
		if (len < 1) { throws new Error("Alert Arguments를 확인하세요."); }
		if (len == 2) {
			if (argument[1].constructor == Function) { title = null; callback = argument[1] || null; }
			else { title = arguments[1] || "알림"; }
		} else if (len == 3) {
			if (arguments[1].constructor !== String && arguments[2].constructor !== Function) { throws new Error("Alert Arguments를 확인하세요."); }
			title = arguments[1] || "알림";
			callback = arguments[2] || null;
		}
		$("#alertMessage").html(message.replace(/(\r|\n)/g, '<br>'));
		%("#alertType").html(title || "알림");
		popup_open($popup);
	};
	
	popup.close = function() {
		popup_close($popup);
		if (callback) {
			window.setTimeout(function() {
				callback;
			}, 100);
		}
	};
	
	return popup;
})({});
</script>