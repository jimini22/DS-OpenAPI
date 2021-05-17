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
	
	getOrganizationList();
	
	var opt = {height: 300, count: false, disableResizeEditor: false, placeholder: "", max: 500};
	EditorUtil.create($("#summernote-edit"), "", opt);
	
	
})





</script>


