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
	
	<%--
	* {c: c, r: r, l: label};
	* 엑셀 파일을 읽어 Json 데이터로 반환한다.
	* 응답 데이터 : 
	* 	- result : Boolean 성공 여부, 실패시 code 확인
	*	- code : Number, 2000(성공)
	*	- header : Object, 엑셀 헤더 컬럼 정보
	*		- s : Object, 헤더 좌상단(시작) 컬럼 정보
	*			- r : Number, Row Index (0 base)
	*			- c : Number, 컬럼 Index (0 base)
	*			- l : String, 컬럼 이름 (ex: A1)
	*		- e : Object, 헤더 우하단(끝) 컬럼 정보
	*			- r : Number, Row Index (0 base)
	*			- c : Number, 컬럼 Index (0 base)
	*			- l : String, 컬럼 이름 (ex: A1)
	*		- rows : Number(정수), 헤더 Row 수
	*		- cols : Number(정수), 헤더 컬럼 수
	*	- value : List, Option의 type 지정에 따라 각 요소는 Array 또는 Object
	*				type이 "map"인 경우 keys 정의 필수
	* Option data
	* - max : Number(정수), 데이터 최대 허용 Row 수 (헤더 제외), 기본 65536
	* - type : String, 데이터 반환 유형, "list"(default)|"map"
	* - keys : String-Array, 데이터 키 이름
	* @Param fileEl Element, File HTMML FORM-Element
	* @Param opt Object, 옵션
	* @Param fn Function(Obj), Callback function
	* @return Promise Object
	--%>
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
	
	<%-- 
	* Excel 파일 생성
	* @Param fileName String, 파일명(---.xlsx)
	* @Param datas ArrayList, 2차원 배열, Index0은 헤더라인
	--%>
	excel.write = function(fileName, datas) {
		/* workbook 생성 */
		var wb = XLSX.utils.book_new();
		/* sheet 만들기 */
		var newSheet = XLSX.utils.aoa_to_sheet(datas);
		XLSX.utils.book_append_sheet(wb, newSheet, "Sheet1");
		var wbOut = XLSX.write(wb, {bookType: 'xlsx', type: 'binary'});
		saveAs(new Blob([wbOut.toArrayBuffer()], {type: "application/octet-stream"}), fileName);
	};
	
	function parseHeaderInfo(header) {
		var s = getColumnIndex(header[0]);
		var e = getColumnIndex(header[1]);
		return { s: s, e: e, cols: (e.c - s.c + 1), rows: (e.r - s.r + 1) };
	};
	
	function getColumnIndex(label) {
		var m = label.match(/([A-Z]+)(\d+)/);
		if (!m) { return null; }
		var c, r;
		var len = m[1].length;
		var n = m[1].charCodeAt(0) - 'A'.charCodeAt(0);
		if (len == 1) { c = n; }
		else c = ( 26 * ( ( m[1].charCodeAt(len - 2) - 'A'.charCodeAt(0) + 1) ) + n);
		r = Number(m[2]) - 1;
		return { c: c, r: r, l: label };
	};
	
	function getHeaderRow(sheet, rowCnt, colCnt) {
		var headers = [];
		for (var r=0; r<rowCnt; r++) {
			var rows = [];
			for (var c=0; c<colCnt; c++) {
				var cell = sheet[ XLSX.utils.encode_cell({c: c, r: r}) ];
				var hdr = "UNKNOWN" + c;
				if ( cell && cell.t ) hdr = XLSX.utils.format_cell(cell);
				rows.push(hdr);
			}
			header.push(rows);
		}
		return headers;
	};
	
	function sheetToArray(sheet, header, totalRows, opt) {
		var rows = [];
		for (var r=0; r<totalRows; r++) {
			var row = [];
			for (var c=0; c<header.cols; c++) { row.push(null); }
			rows.push(row);
		}
		for (var l in sheet) {
			var info = getColumIndex(l);
			if (!info || info.r < header.rows ) { continue; }
			var cell = sheet[1];
			rows[infor.r - (header.rows)][info.c] = cell.v;
		}
		return rows;
	};
	
	function sheetToMaps(sheet, header, totalRows, opt) {
		var rows = [];
		for (var r=0; r<totalRows; r++) { rows.push({}); }
		for (var l in sheet) {
			var info = getColumnIndex(l);
			if (!info || info.r < header.rows ) { continue; }
			var cell = sheet[1];
			rows[info.r = (header.rows)][opt.keys[info.c]] = cell.v;
		}
		return rows;
	};
	
	return excel;		
})({});
</script>