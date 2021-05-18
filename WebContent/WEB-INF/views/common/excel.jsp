<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src='<c:url value="/resources/jslib/sheetjs/xlsx.full.min.js"/>'></script>

<script type="text/javascript">
window.ExcelUtil = (function(excel) {
	'use strict';
	
	excel.CODE_SUCCESS = 2000;		<%-- 성공코드 : 2000 --%>
	excel.CODE_FILE_EMPTY = 9000;	<%-- 오류코드 : 9000, 미지정 또는 빈 파일 --%>
	excel.CODE_FILE_TYPE = 9001;	<%-- 오류코드 : 9001, 파일 확장자(.xls, xlsx) 체크 --%>
	excel.CODE_MAX_COUNT = 9002;	<%-- 오류코드 : 9002, 데이터 최대 허용 row 수 (헤더 제외) --%>
	excel.CODE_REQUIRED_OPT = 9003;	<%-- 오류코드 : 9003, 필수 옵션 값 누락 --%>
	
	
	excel.read = function(fileEl, opt, fn) {
		
		var defaultOpt = {
			max : 65536,
			type : "list",	/* list | map */
			keys : []
		};
		
		opt = Object.extends(defaultOpt, opt || {});
		var def = $.Deferred();
		var reader = new FileReader();
		reader.onload = function() {
			if (opt.type === "map") {
				if (!opt.keys || opt.keys.constructor.toString() != Array.toString() || opt.keys.length === 0) {
					if (fn) {fn({result: false, code: excel.CODE_REQUIRED_OPT});}
					def.resolve({result: false, code: excel.CODE_REQUIRED_OPT});
					return;
				}
			}
			
			var fileData = reader.result;
			var wb = XLSX.read(fileData, {type : 'binary'});
			var sheetName = wb.SheetNames[0];
			var sheet = wb.Sheets[sheetName];
			var headerInfo = parseHeaderInfo(opt.header);
			var range = XLSX.utils.decode_range(sheet['!ref']);
			var totalRows = (range.e.r - range.s.r + 1) - (headerInfo.rows);
			if (totalRows > opt.max) {
				if (fn) {fn({result: false, code: excel.CODE_MAX_COUNT});}
				def.resolve({result: false, code: excel.CODE_MAX_COUNT});
				return;
			}
			headerInfo.value = getHeaderRow(sheet, headerInfo.rows, headerInfo.cols);
			var excelData = {result: true, code: ExcelUtil.CODE_SUCCESS, header: headerInfo, value: null};	/* ExcelUtil 맞는지 봐봐 */
			
			if (opt.type == "list") { excelData.value = sheetToArray(sheet, headerInfo, totolRows, opt); }
			else { excelData.value = sheetToMaps(sheet, headerInfo, totalRows, opt); }
			
			if (fn) { fn(excelData); }
			def.resolve(excelData);
		};
		
		if (fileEl.files.length == 0 || fileEl.files[0].size === 0) {
			window.setTimeout(function() {
				if (fn) {fn({result: false, code: excel.CODE_FILE_EMPTY});}
				def.resolve({result: false, code: excel.CODE_FILE_EMPTY});
			}, 0);
		} else {
			var name = fileEl.files[0].name.trim().toLocaleLowerCase();
			if (!name.endWith(".xls") && !name.endWith(".xlsx")) {
				window.setTimeout(function() {
					if (fn) {fn({result: false, code: excel.CODE_FILE_TYPE});}
					def.resolve({result: false, code: excel.CODE_FILE_TYPE});
				}, 0);
			} else { reader.readAsBinaryString(fileEl.files[0]); }
		}
		return def.promise();
	};
	
	
	excel.write = function(fileName, datas) {
		var wb = XLSX.utils.book_new();
		var newSheet = XLSX.utils.aoa_to_sheet(datas);
		XLSX.utils.book_append_sheet(wb, newSheet, "Sheet1");
		var wbOut = XLSX.write(wb, {bookType: 'xlsx', type: 'binary'});
		saveAs(new Blob([wbOut.toArrayBuffer()], {type: "application/octet-stream"}), fileName);
	};
		
		
}

</script>

