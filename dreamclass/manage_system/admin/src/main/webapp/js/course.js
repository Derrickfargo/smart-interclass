//页面初始化设置
$(document).ready(function() {
	$("#courseForm").validate({
		submitHandler:function(form){
			var flag = true;
			//学校名称
//			if (!new RegExp(/^[\u4e00-\u9fa5]{2,8}$/).test($("#courseName").val())) {
//				$("#checkCourseNameTip").removeClass("hidden");
//				flag = false;
//			}
//			//检查身份证格式是否正确
//			var idCarNo = new RegExp(/^\d{17}([0-9]|X)$|^(\d{15})$/);
//			if (!idCarNo.test($("#idcard").val())) {
//				$("#checkCourseIdTip").removeClass("hidden");
//				flag = false;
//			}
//			//检查联系方式格式是否正确
//			var isTelNum = new RegExp(/^\d{11}/);
//			if (!isTelNum.test($("#courseTel").val())) {
//				$("#checkCourseTelTip").removeClass("hidden");
//				flag = false;
//			}
//
//			if ($("#courseName").val() == "") {
//				$('#checkCourseCourseTip').removeClass('hidden');
//				flag = false;
//			}
			
			if (flag) {
				form.submit();
			}
		}
	});
});


// 搜索教师信息
function searchCourse(pageNum) {
	$("#pageNum").val(pageNum);
	$("#searchForm").attr("action", _path + "/course/list");
	$("#searchForm").submit();
}

// 页面跳转，查看教师详情或者修改教师信息
function modifyCourse(courseId) {
	$("#courseId").val(courseId);
	$("#pageType").val(pageType);
	$("#searchForm").attr("action", _path + "/course/" + opType);
	$("#searchForm").submit();
}


function deleteCourse(courseId){
	if(confirm("确认要删除本条记录吗?")){
		$("#courseId").val(courseId);
		$("#searchForm").attr("action", _path + "/course/delete");
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
function saveCourse() {
	$("#courseForm").submit();
}

