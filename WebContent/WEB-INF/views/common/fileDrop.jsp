<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script type="text/javascript">
window.FileDrop = (function(drop){

	<%--
	* OPTION 설정 값
	* dropLimitSize		Number		: 한번에 drop할 수 있는 파일 갯수 제한 [0 : 무제한]
	* fileReaderType	String		: fileReader 종류, blob/text/url [default : blob]
	* beforeDropFn		function()	: Drop 처리시 파일 추가 전 수행 함수
	* ex) FileDrop.setOption({ dropLimitSize : 1 });
	--%>
	var option = { dropLimitSize: 0, fileReaderType: 'blob', beforeDropFn: null };
	
	drop.setOption = function(opt) {
		var keys = Object.keys(opt);
		keys.forEach(function(item, idx) {
			option[item] = opt[item];
		});
	};
	
	<%--
	* Drop 파일 설정
	* 응답 데이터
	*	- file File
	*	- mimeType String
	*	- blob Blob, 파일 내용
	*
	* @param targetId String, Drop 이벤트를 받을 타겟 Element(DIV..)의 아이디
	* @param cssName String, dragenter/dragover/dragleave 효과 css class 이름
	* @param callback Function(Object), 파일 드롭 시 파일을 전달 받을 콜백 함수
	--%>
	drop.setById = function(targetId, cssName, callback) {
		drop.setByObject($("#"+targetId), cssName||"", callback);	
	};
	
	drop.setByObject = function($target, cssName, callback) {
		$target.off();
		
		$target.on("dragenter", {css:cssName||""}, function(e){
			e.stopPropagation();
			e.preventDefault();
			if(e.data.css) $target.addClass(e.data.css);
		});
		
		$target.on("dragover", {css:cssName||""}, function(e){
			e.stopPropagation();
			e.preventDefault();
			if(e.data.css) $target.addClass(e.data.css);
		});
		
		$target.on("dragleave", {css:cssName||""}, function(e){
			e.stopPropagation();
			e.preventDefault();
			if(e.data.css) $target.removeClass(e.data.css);
		});
		
		$target.on("drop", {css:cssName||"", callback:callback}, function(e){
			e.preventDefault();
			var $this = $(this);
			if(e.data.css) $this.removeClass(e.data.css);
			var callback = e.data.callback||null;
			var files = e.originalEvent.dataTransfer.files;
			if(files != null && files.length > 0) {
				var type = option.fileReaderType;
				var size = option.dropLimitSize;
				if (size != 0 && files.length > size) {
					Alert.show("파일은 최대 "+size+"개씩 추가해 주세요");
					return;
				}
				if (option.beforeDropFn != null && typeof option.beforeDropFn === 'function') {
					option.beforeDropFn();
				}
				for (var i=0; i<files.length; i++) {
					switch (type) {
					case "text" :
						FileUtil.readAsText(files[i], callback);
						break;
					case "url" :
						FileUtil.readAsDataURL(files[i], callback);
						break;
					default :
						FileUtil.readAsBlob(files[i], callback);
					}
				}
			}
		});
	};
	return drop;
})({});
</script>