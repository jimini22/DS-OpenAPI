<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/fileDrop.jsp"%>

<script type="text/javascript">

var BoardCommon = (function (fn) {
	
	<%-- (상세페이지) 첨부파일 리스트 --%>
	fn.listAttach = function(postId) {
		var url = "/rest/posts/files/" + postId;
		Ajax.doGet(url).then(function(resData) {
			showAttach(resData.responseData.data);
		}).catch(function(resData) {
			Alert.show("첨부파일 리스트 보기 실패");
		});
	}
	
	function showAttach(data) {
		
		if (data.fileList.length == 0) {
			$(".mini-layer-popup").hide();
		} else if (data.fileList.length == 1) {
			$(".add-file").show();
			$(".mini-layer-popup").remove();
			$("#dtlAttachFile").removeClass("more-file");
			$("#dtlAttachFile").data("attach_id", data.fileList[0].id);
			$("#dtlAttachFile").html(data.fileList[0].fileName);
		} else {
			$(".add-file").show();
			$("#dtlAttachFile").append(data.fileList[0].fileName);
			var html = [];
			for (n in data.fileList) {
				var attach = data.fileList[n];
				html.push('<a href="javascript:void(0);" class="add-files" data-attach_id="' + (attach.id||"") + '">' + (attach.fileName||"") + '</a>');
			}
			$("#dtlAttachFileList").html(html);
		}
		
	}
	<%-- //(상세페이지) 첨부파일 리스트 --%>
	
	<%-- 업로드할 목록 보여주기 (첨부파일 info settion) --%>
	function setAttachFileInfo(info, $fileList) {
		
		if(!getFileValidation(info.file, 10, "")) {
			return;
		}
		
		var $file = $('<tr class="file-line"><td>' + info.file.name + '</td>' +
					'<td class="size">' + info.file.size.byteString("K") + '</td>' +
					'<td><a href="javascript:void(0);" class="delete-button new-one">삭제</a></td></tr>');
		if ($fileList.find('tr').length > 0) {
			$fileList.find('.file-drop-area').hide();
		}
		$fileList.append($file);
		$file.data("info", info);
		
	}
	
	<%-- 업로드할 목록 보여주기 (첨부파일 업로드 > [찾아보기]버튼) --%>
	fn.setAttachInputFile = function(files, $fileList) {
		if (files.length != 0) {
			var file = files[0];
			FileUtil.readAsBlob(file, function(info){
				setAttachFileInfo(info, $fileList);
			});
			fileInit();
		}
	}
	
	<%-- 업로드할 목록 보여주기 (첨부파일 업로드 > drag&drop) --%>
	fn.setAttachDropFile = function($fileList) {
		FileDrop.setByObject($(".table-attach-file"), "filedragenter", function(info) {
			setAttachFileInfo(info, $fileList);
		});
		fileInit();
	}
	
	<%-- 업로드할 목록 보여주기 (첨부파일 업로드 > 업로드할 파일 삭제) --%>
	fn.attachFileRemove = function($fileList) {
		$fileList.off().on("click", ".delete-button.new-one", function() {
			$(this).closest('tr').remove();
			if ($fileList.find('tr').length == 1) {
				$fileList.find('tr').show();
			}
		});
		fileInit();
	}
	
	<%-- (수정페이지) 기존 첨부파일 리스트 --%>
	fn.preAttachFileList = function(postId, $fileList) {
		var url = "/rest/posts/files/" + postId;
		Ajax.doGet(url).then(function(resData) {
			showPreAttachFileList(resData.responseData.data, postId, $fileList);
		}).catch(function(resData) {
			Alert.show("첨부파일 리스트 보기 실패");
		});
	}
	
	function showPreAttachFileList(data, postId, $fileList) {
		if (data.fileList.length == 0) {
			$fileList.find('.file-drop-area').show();
			return;
		} else {
			$fileList.find('.file-drop-area').hide();
			var html = [];
			for (n in data.fileList) {
				var attach = data.fileList[n];
				html.push('<tr>');
				html.push(	'<td class="attachId">' + (attach.fileName||"") + '</td>');
				html.push(	'<td class="size">' + (attach.fileSize||"").byteString("K") + '</td>');
				html.push(	'<td><a href="javascript:void(0);" class="delete-button old-one" data-attach_id="' + (attach.id||"") + '" data-post_id="' + postId + '">삭제</a></td>');
				html.push('</tr>');
			}
			$fileList.append(html.join(''));
		}
		
		<%-- 기존 첨부파일 리스트 중 파일 삭제 --%>
		$fileList.off().on("click", ".delete-button.old-one", function() {
			var attachId = $(this).data('attach_id');
			var url = "/rest/attach/file/" + attachId + "/delete";
			var toDeleteData = $(this);
			Ajax.doPost(url).then(function(resData) {
				if (resData.responseData.code === 2000 && resData.responseData.data === true) {
					$(toDeleteData).closest('tr').remove();
					if ($fileList.find('tr').length == 1) {
						$fileList.find('tr').show();
					}
				} else {
					Alert.show("파일 삭제 중 오류가 발생했습니다.\n다시 시도하여 주십시오.", "오류");
				}
			}).catch(function(resData) {
				Alert.show("파일 삭제 중 오류가 발생했습니다.\n다시 시도하여 주십시오.", "오류");
			});
		})
	}
	<%-- //(수정페이지) 기존 첨부파일 리스트 --%>
	
	<%-- 첨부파일 업로드 --%>
	fn.addAttachFileList = function($fileList, postId, smsData, fileData, targetUrl) {
		var $files = $fileList.find('tr.file-line');
		
		<%-- 업로드할 파일 없을 때 --%>
		if ($files.length == 0) {
			if (typeof smsSend == "function") {
				smsSend(smsData, true);
			} else if (targetUrl) {
				window.location.href = targetUrl + $.param(getSearchParam());
			}
			return;
		}
		
		<%-- 업로드할 파일 있을 때 --%>
		var formData = new FormData();
		for (var n in fileData) {
			formData.append(n, fileData[n]);
		}
		$files.each(function(n, el) {
			var info = $(el).data("info");
			if (info) {
				formData.append("file", info.blob, info.file.name);
			}
		});
		
		var url = ($files.length > 1) ? "/rest/attach/files" : "/rest/attach/file";
			Ajax.doFormData(url, formData, null).then(function(resData) {
				if (typeof smsSend == "function") {
					smsSend(smsData, resData.responseData.code == 2000)
				} else if (targetUrl) {
					window.location.href = targetUrl + $.param(getSearchParam());
				}
			}).catch(function(resData) {
				if (typeof smsSend == "function") {
					smsSend(smsData, false)
				} else if (targetUrl) {
					window.location.href = targetUrl + $.param(getSearchParam());
				}
			});
	}
	<%-- //첨부파일 업로드 --%>
	
	<%-- 첨부파일 업로드 초기화 --%>
	function fileInit() {
		if (Browser.ie) {
			$("#attach-file").replaceWith($("#attach-file").clone(true))
		} else {
			$("#attach-file").val("");
		}
	}
	<%-- //첨부파일 업로드 초기화 --%>
	
	<%-- 등록 가능한 첨부파일 형식 확인 --%>
	function getFileValidation(file, size, extension) {
		
		if (!file) { return false; }
		
		var maxsize = size * 1024 * 1024;
		
		if (file.size > maxsize) {
			Alert.show("첨부파일 사이즈는 " + size + "MB 이내로 등록 가능합니다.");
			return false;
		}
		
		var extensionsRgExp = RegExp("(\.doc|\.docx|\.xls|\.xlsx|\.ppt|\.pptx|\.hwp|\.pdf|\.gif|\.jpg|\.png|\.jpeg)$");
		var extensionsTxt = "";
		if (extension) {
			var extensions = extension.split(",");
			$.each(extensins, function(index, item) {
				if (index ==0) {
					extensionsTxt = "\\." + item;
				} else {
					extensionsTxt = extensionsTxt + "|\\." + item;
				}
			});
			extensionsRgExp = RegExp("(" + extensionsTxt + ")$");
		}
		
		if (!extensionsRgExp.test(file.name.toLowerCase())) {
			Alert.show("파일 확장자 형식을 확인해 주세요.");
			return false;
		}
		
		return true;
	}
	
	<%-- 첨부파일 다운로드 --%>
	fn.attachDownload = function(attachId) {
		if (attachId) {
			Ajax.doGet("/rest/attach/file/" + attachId + "/exist").then(function(resData) {
				if (resData.responseData.code == 2000) {
					var existData = resData.responseData.data.exist;
					if (existData == true) {
						location.href = "/rest/attach/file/" + attachId + "/download";
					} else {
						Alert.show("파일을 찾을 수 없습니다.\n관리자에게 문의하세요.");
					}
				}
			}).catch(function(resData) {
				Alert.show("첨부파일 다운로드 실패");
			});
		}
	}
	<%-- //첨부파일 다운로드 --%>
	
	return fn;
	
})({});
</script>