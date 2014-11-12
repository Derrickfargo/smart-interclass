//页面初始化设置
$(document).ready(function() {
	$("#studentForm").validate({
		rules:{
			imei:{
				remote:{
					type:"post",
					url:"check",
					data:{
						studentId:function(){
							return $("#studentId").val();
						},
						imei:function(){
							return $("#imei").val();
						}
					}
				}
			}
		},
		messages:{
			imei:"该mac地址已被其他用户使用"
		},
		submitHandler:function(form){
			var flag = true;
			//学校名称
//			if (!new RegExp(/^[\u4e00-\u9fa5]{2,8}$/).test($("#studentName").val())) {
//				$("#checkStudentNameTip").removeClass("hidden");
//				flag = false;
//			}
//			//检查身份证格式是否正确
//			var idCarNo = new RegExp(/^\d{17}([0-9]|X)$|^(\d{15})$/);
//			if (!idCarNo.test($("#idcard").val())) {
//				$("#checkStudentIdTip").removeClass("hidden");
//				flag = false;
//			}
			//检查班级
			if(!new RegExp(/^\d{1,9}$/).test($("#classNumber").val())){
				$("#checkClassNumberTip").removeClass("hidden");
				flag=false;
			}
			//检查联系方式格式是否正确
			var isTelNum = new RegExp(/^\d{11}/);
			if (!isTelNum.test($("#phone").val())) {
				$("#checkPhoneTip").removeClass("hidden");
				flag = false;
			}
			//姓名
			if (!new RegExp(/^[\u4e00-\u9fa5]{2,8}$/).test($("#name").val())) {
				$("#checkNameTip").removeClass("hidden");
				flag = false;
			}
			//监护人
			if (!new RegExp(/^[\u4e00-\u9fa5]{2,8}$/).test($("#guardian").val())) {
				$("#checkGuardianTip").removeClass("hidden");
				flag = false;
			}
			//mac地址格式
			if(!new RegExp(/^[0-9a-fA-F]{2}-[0-9a-fA-F]{2}-[0-9a-fA-F]{2}-[0-9a-fA-F]{2}-[0-9a-fA-F]{2}-[0-9a-fA-F]{2}$/)
			.test($("#imei").val())){
				$("#checkIMEITip").removeClass("hidden");
				flag=false;
			}
			if (flag) {
				form.submit();
			}
		}
	});
});


// 搜索教师信息
function searchStudent(currentPage) {
	$("#currentPage").val(currentPage);
	$("#searchForm").submit();
}

// 页面跳转，查看教师详情或者修改教师信息
function modifyStudent(studentId) {
	$("#studentId").val(studentId);
//	$("#pageType").val(pageType);
	$("#searchForm").attr("action", _path + "/student/edit");
	$("#searchForm").submit();
}


function deleteStudent(studentId){
	if(confirm("确认要删除本条记录吗?")){
		$("#studentId").val(studentId);
		$("#searchForm").attr("action", _path + "/student/delete");
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
function saveStudent() {
	$("#studentForm").submit();
}

