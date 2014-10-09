//页面初始化设置
$(document).ready(function() {
	$("#classForm").validate({
		submitHandler:function(form){
			var flag = true;
			//学校名称
//			if (!new RegExp(/^[\u4e00-\u9fa5]{2,8}$/).test($("#className").val())) {
//				$("#checkClassNameTip").removeClass("hidden");
//				flag = false;
//			}
//			//检查年份是否正确
			var year = new RegExp(/^\d{4,4}$/);
			if (!year.test($("#year").val())) {
				$("#checkYearTip").removeClass("hidden");
				flag = false;
			}
//			//检查联系方式格式是否正确
//			var isTelNum = new RegExp(/^\d{11}/);
//			if (!isTelNum.test($("#classTel").val())) {
//				$("#checkClassTelTip").removeClass("hidden");
//				flag = false;
//			}
//
//			if ($("#className").val() == "") {
//				$('#checkClassClassTip').removeClass('hidden');
//				flag = false;
//			}
			
			if (flag) {
				form.submit();
			}
		}
	});
});


// 搜索教师信息
function searchClass(currentPage) {
	$("#currentPage").val(currentPage);
	$("#searchForm").submit();
}

// 页面跳转，查看教师详情或者修改教师信息
function modifyClass(classId) {
	$("#classId").val(classId);
	$("#searchForm").attr("action", _path + "/class/edit");
	$("#searchForm").submit();
}


function deleteClass(classId){
	if(confirm("确认要删除本条记录吗?")){
		$("#classId").val(classId);
		$("#searchForm").attr("action", _path + "/class/delete");
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
function saveClass() {
	$("#classForm").submit();
}

