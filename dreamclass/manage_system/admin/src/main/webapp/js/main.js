// 页面初始化设置
$(document).ready(function() {
	$(".datetimepicker1").datetimepicker({
		"autoclose" : true,
		"minView" : 2,
		"maxView" : 4,
		"format" : 'yyyy-mm-dd',
		"language" : 'zh-CN'
	});

	$(".datetimepicker2").datetimepicker({
		"autoclose" : true,
		"minView" : 2,
		"maxView" : 4,
		"format" : 'yyyy-mm-dd',
		"language" : 'zh-CN'
	});
});

// 切换面板(详情页面、新增页面、修改页面)
function switchPanel(obj) {
	var vpanel = $(obj).parent().find(".panel-body");
	if (vpanel.hasClass("hidden")) {
		vpanel.removeClass("hidden");
	} else {
		vpanel.addClass("hidden");
	}
}

// 清空form表单
function emptyForm(fromId) {
	$("#" + fromId).find(':input').not(':hidden,:button, :submit, :reset').val(
			'').removeAttr('checked').removeAttr('selected');
}

/**
 * 取消操作二次确认
 * 
 * @param msg
 */
function goback(msg) {
	if (window.confirm("是否放弃本次" + msg + "的操作？")) {
		window.history.go(-1);
	}
}

function checkPhoto(obj, delBtn) {
	var fileValue = $(obj).val();
	if (fileValue != "") {
		if(!/\.(jpg|jpeg|png|gif|bmp|JPG|JPEG|PNG|GIF|BMP)$/.test(fileValue)) {
			alert("图片格式必须是.gif,jpeg,jpg,bmp中的一种！");
			$(delBtn).click();
			return false;
		}
	}
	return true;
}
