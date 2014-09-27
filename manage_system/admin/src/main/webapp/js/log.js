//页面初始化设置
$(document).ready(function() {
	$("#logForm").validate({
		submitHandler:function(form){
			var flag = true;
			//学校名称
//			if (!new RegExp(/^[\u4e00-\u9fa5]{2,8}$/).test($("#logName").val())) {
//				$("#checkLogNameTip").removeClass("hidden");
//				flag = false;
//			}
//			//检查身份证格式是否正确
//			var idCarNo = new RegExp(/^\d{17}([0-9]|X)$|^(\d{15})$/);
//			if (!idCarNo.test($("#idcard").val())) {
//				$("#checkLogIdTip").removeClass("hidden");
//				flag = false;
//			}
//			//检查联系方式格式是否正确
//			var isTelNum = new RegExp(/^\d{11}/);
//			if (!isTelNum.test($("#logTel").val())) {
//				$("#checkLogTelTip").removeClass("hidden");
//				flag = false;
//			}
//
//			if ($("#logName").val() == "") {
//				$('#checkLogLogTip').removeClass('hidden');
//				flag = false;
//			}
			
			if (flag) {
				form.submit();
			}
		}
	});
});


// 搜索
function searchLog(pageNum) {
	$("#pageNum").val(pageNum);
	$("#searchForm").submit();
}

// 页面跳转，查看教师详情或者修改教师信息
function modifyLog(logId) {
	$("#logId").val(logId);
	$("#pageType").val(pageType);
	$("#searchForm").attr("action", _path + "/log/" + opType);
	$("#searchForm").submit();
}


function deleteLog(logId){
	if(confirm("确认要删除本条记录吗?")){
		$("#logId").val(logId);
		$("#searchForm").attr("action", _path + "/log/delete");
		$("#searchForm").submit();
	}
}

function mouseOver(obj){
	$(obj).css("background","#efefef");
	$(obj).css("cursor","pointer");
}

function mouseOut(obj){
	$(obj).css("background","#fff");
	$(obj).css("cursor","default");
}

// 保存学校信息
function saveLog() {
	$("#logForm").submit();
}

