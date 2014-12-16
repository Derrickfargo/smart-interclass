//页面初始化设置
$(document).ready(function() {
	$("#versionForm").validate({
		submitHandler:function(form){
			var flag = true;
			//学校名称
//			if (!new RegExp(/^[\u4e00-\u9fa5]{2,8}$/).test($("#versionName").val())) {
//				$("#checkVersionNameTip").removeClass("hidden");
//				flag = false;
//			}
//			//检查身份证格式是否正确
//			var idCarNo = new RegExp(/^\d{17}([0-9]|X)$|^(\d{15})$/);
//			if (!idCarNo.test($("#idcard").val())) {
//				$("#checkVersionIdTip").removeClass("hidden");
//				flag = false;
//			}
//			//检查联系方式格式是否正确
//			var isTelNum = new RegExp(/^\d{11}/);
//			if (!isTelNum.test($("#versionTel").val())) {
//				$("#checkVersionTelTip").removeClass("hidden");
//				flag = false;
//			}
//
//			if ($("#versionName").val() == "") {
//				$('#checkVersionVersionTip').removeClass('hidden');
//				flag = false;
//			}
			//检查上传版本文件
			if($("#type").val()==2){
				if(!new RegExp(/\.[a|A][p|P][k|K]$/).test($("#file").val())){
					$("#fileTip").removeClass("hidden");
					flag=false;
				}
			}
			if($("#type").val()==1){
				if(!new RegExp(/\.[e|E][x|E][e|E]$/).test($("#file").val())){
					$("#fileTips").removeClass("hidden");
					flag=false;
				}
			}
			if (flag) {
				form.submit();
			}
		}
	});
});


// 搜索
function searchVersion(pageNum) {
	$("#pageNum").val(pageNum);
	$("#searchForm").attr("action", _path + "/version/list");
	$("#searchForm").submit();
}

// 页面跳转，查看教师详情或者修改教师信息
function modifyVersion(versionId) {
	$("#id").val(versionId);
	$("#pageType").val(pageType);
	$("#searchForm").attr("action", _path + "/version/" + opType);
	$("#searchForm").submit();
}


function deleteVersion(versionId){
	if(confirm("确认要删除本条记录吗?")){
		$("#id").val(versionId);
		$("#searchForm").attr("action", _path + "/version/delete");
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
function saveVersion() {
	$("#versionForm").submit();
}

function showHidden(){
	$("#fileTip").addClass("hidden");
	$("#fileTips").addClass("hidden");
}

