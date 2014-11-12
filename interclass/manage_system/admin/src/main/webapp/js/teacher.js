//页面初始化设置
$(document).ready(function() {	
	$("#teacherForm").validate({
		//新增教师时对登录名,身份证号的重复验证
		rules:{
			uname:{
				remote:{
					type:"post",
					url:"check",
					data:{
						uname:function(){
							return $("#teacherUName").val();
						},
						id:function(){
							return $("#teacherId").val();
						}
					}
				}
			},
			idcard:{
				remote:{
					type:"post",
					url:"check",
					data:{
						idcard:function(){
							return $("#idcard").val();
						},
						id:function(){
							return $("#teacherId").val();
						}
					}
				}
			}
		
		},
		messages:{
			uname:{
				remote:"登录名已存在",
			},
			idcard:{
				remote:"身份证号码已注册"
			}
			
			
		},
		submitHandler:function(form){
			var flag = true;
			//登陆名
			if (!new RegExp(/^[a-zA-Z][a-zA-Z0-9]{1,15}$|(^[\u4e00-\u9fa5]{2,8}$)/).test($("#teacherUName").val())) {
				$("#checkTeacherUNameTip").removeClass("hidden");
				flag = false;
			}
			//姓名
			if (!new RegExp(/^[\u4e00-\u9fa5]{2,8}$/).test($("#teacherName").val())) {
				$("#checkTeacherNameTip").removeClass("hidden");
				flag = false;
			}
			//检查身份证格式是否正确
			var idCarNo = new RegExp(/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/);
			if (!idCarNo.test($("#idcard").val())) {
				$("#checkTeacherIdTip").removeClass("hidden");
				flag = false;
			}
			//检查联系方式格式是否正确
			var isTelNum = new RegExp(/^\d{11}$/);
			if (!isTelNum.test($("#teacherTel").val())) {
				$("#checkTeacherTelTip").removeClass("hidden");
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

function modifyTeacher(teacherId) {
	$("#teacherId").val(teacherId);
	$("#searchForm").attr("action", _path + "/teacher/edit");
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

