//页面初始化设置
$(document).ready(function() {
	$("#teacherForm").validate({
		submitHandler:function(form){
			var flag = true;
			//登陆名
			if (!new RegExp(/^[a-zA-Z][a-zA-Z0-9]{1,15}$/).test($("#teacherUName").val())) {
				$("#checkTeacherUNameTip").removeClass("hidden");
				flag = false;
			}
			//姓名
			if (!new RegExp(/^[\u4e00-\u9fa5]{2,8}$/).test($("#teacherName").val())) {
				$("#checkTeacherNameTip").removeClass("hidden");
				flag = false;
			}
			//检查身份证格式是否正确
			var idCarNo = new RegExp(/^\d{17}([0-9]|X)$|^(\d{15})$/);
			if (!idCarNo.test($("#idcard").val())) {
				$("#checkTeacherIdTip").removeClass("hidden");
				flag = false;
			}
			//检查联系方式格式是否正确
			var isTelNum = new RegExp(/^\d{11}/);
			if (!isTelNum.test($("#teacherTel").val())) {
				$("#checkTeacherTelTip").removeClass("hidden");
				flag = false;
			}

			if ($("#teacherName").val() == "") {
				$('#checkTeacherTeacherTip').removeClass('hidden');
				flag = false;
			}
			
			if (flag) {
				form.submit();
			} else {
				return false;
			}
		}
	});
});


// 搜索教师信息
function searchTeacher(pageNum) {
	$("#pageNum").val(pageNum);
	$("#searchForm").submit();
}

function modifyTeacher(teacherId, opType, pageType) {
	$("#teacherId").val(teacherId);
	$("#pageType").val(pageType);
	$("#searchForm").attr("action", _path + "/teacher/" + opType);
	$("#searchForm").submit();
}


function deleteTeacher(teacherId){
	if(confirm("确认要删除本条记录吗?")){
		$("#teacherId").val(teacherId);
		$("#searchForm").attr("action", _path + "/teacher/delete");
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

// 保存教师信息
function saveTeacher() {
	$("#teacherForm").submit();
}

