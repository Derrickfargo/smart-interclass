//页面初始化设置
$(document).ready(function() {
	$("#schoolForm").validate({
		submitHandler:function(form){
			var flag = true;
			//学校名称
//			if (!new RegExp(/^[\u4e00-\u9fa5]{2,8}$/).test($("#schoolName").val())) {
//				$("#checkSchoolNameTip").removeClass("hidden");
//				flag = false;
//			}
//			//检查身份证格式是否正确
//			var idCarNo = new RegExp(/^\d{17}([0-9]|X)$|^(\d{15})$/);
//			if (!idCarNo.test($("#idcard").val())) {
//				$("#checkSchoolIdTip").removeClass("hidden");
//				flag = false;
//			}
//			//检查联系方式格式是否正确
//			var isTelNum = new RegExp(/^\d{11}/);
//			if (!isTelNum.test($("#schoolTel").val())) {
//				$("#checkSchoolTelTip").removeClass("hidden");
//				flag = false;
//			}
//
//			if ($("#schoolName").val() == "") {
//				$('#checkSchoolSchoolTip').removeClass('hidden');
//				flag = false;
//			}
			
			if (flag) {
				form.submit();
			} else {
				alert()
				return false;
			}
		}
	});
});


// 搜索教师信息
function searchSchool(currentPage) {
	$("#currentPage").val(currentPage);
	$("#searchForm").submit();
}

// 页面跳转，查看教师详情或者修改教师信息
function modifySchool(schoolId, opType, pageType) {
	$("#schoolId").val(schoolId);
	$("#pageType").val(pageType);
	$("#searchForm").attr("action", _path + "/school/" + opType);
	$("#searchForm").submit();
}


function deleteSchool(schoolId){
	if(confirm("确认要删除本条记录吗?")){
		$("#schoolId").val(schoolId);
		$("#searchForm").attr("action", _path + "/school/delete");
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
function saveSchool() {
	$("#schoolForm").submit();
}

