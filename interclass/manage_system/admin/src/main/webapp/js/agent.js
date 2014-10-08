//页面初始化设置
$(document).ready(function() {
	$("#agentForm").validate({
		submitHandler:function(form){
			// 如果密码和确认密码不一致
			if ($("#password").val() != $("#confirmpass").val()) {
				$('#passTip').removeClass('hidden');
				return false;
			}
			
			// 如果货代用户名已存在
			if ($('#agent_exist_flag').val() == "1") {
				$('#checkAgentTip').removeClass('hidden');
				return false;
			}
			
			form.submit();
		}
	});
});

//检查密码和确认密码
function checkpass() {
	if ($("#password").val() == $("#confirmpass").val()) {
		$('#passTip').addClass('hidden');
	}
}

// 搜索货代
function searchAgent(currentPage) {
	$("#currentPage").val(currentPage);
	$("#searchForm").submit();
}

// 页面跳转，查看货代详情或者修改货代信息
function jump(agentId, pageType) {
	$("#agentId").val(agentId);
	$("#searchForm").attr("action", _path + "/agent/" + pageType);
	$("#searchForm").submit();
}

// 全选货代
function checkAllAgent(boxObj) {
	if ($(boxObj).prop("checked") == true) {
		$("input[name='agentBox']").prop("checked", true);
	} else {
		$("input[name='agentBox']").removeAttr("checked");
	}
}

// 批量注销或者激活货代
function batchAgent(type) {
	$("#opType").val(type);
	var opType = "";
	if (type == "0") {
		opType = "激活";
	} else {
		opType = "注销";
	}

	var hasChecked = false;
	$("input[name='agentBox']").each(function() {
		if ($(this).prop("checked") == true) {
			hasChecked = true;
			return false;
		}
	});

	if (hasChecked == false) {
		alert("请选择要" + opType + "的货代！");
		return;
	}

	if (window.confirm("确定" + opType + "这些货代吗？")) {
		$.ajax({
			url : _path + "/agent/batch",
			type : "POST",
			datatype : "json",
			data : $('#searchForm').serialize(),
			success : function(data) {
				if (data.status == 0) {
					alert(data.msg);
					$("#searchForm").submit();
				} else {
					alert(data.msg);
				}
			},
			error : function(data) {
				alert(data.msg);
			}
		});
	}
}

// 注销或者激活货代信息
function cancelAgent(agentId, type) {
	var opType = "";
	if (type == "0") {
		opType = "激活";
	} else {
		opType = "注销";
	}
	if (window.confirm("确定" + opType + "该货代吗？")) {
		$.ajax({
			url : _path + "/agent/cancel",
			type : "POST",
			datatype : "json",
			data : {
				"agentId" : agentId,
				"type" : type
			},
			success : function(data) {
				if (data.status == 0) {
					alert(data.msg);
					$("#searchForm").submit();
				} else {
					alert(data.msg);
				}
			},
			error : function(data) {
				alert(data.msg);
			}
		});
	}
}

// 新增和修改页面填写完成后提交
function editAgent(optype) {
	if (optype == "0") {
		$("#agentForm").attr("action", _path + "/agent/add");
	} else {
		$("#agentForm").attr("action", _path + "/agent/modify");
	}
	$("#agentForm").submit();
}

// 修改密码
function modifyPass(type) {
	var password = "";
	var typeName = "";
	if (type == "0") {
		var original_pass = $.trim($("input[name='password']").val());
		var confirmpass = $.trim($("input[name='confirmpass']").val());
		if (original_pass == "") {
			$('#passTip1').removeClass('hidden');
			return;
		} else if (confirmpass == "") {
			$('#passTip2').removeClass('hidden');
			return;
		} else if (original_pass != confirmpass) {
			$('#passTip3').removeClass('hidden');
			return;
		}
		
		$('#passTip1').addClass('hidden');
		$('#passTip2').addClass('hidden');
		$('#passTip3').addClass('hidden');

		password = hex_md5(original_pass);
		typeName = "修改密码";
	} else {
		typeName = "重置为系统密码";
	}

	if (window.confirm("确定" + typeName + "吗？")) {
		$.ajax({
			url : _path + "/agent/password",
			type : "POST",
			datatype : "json",
			data : {
				"id" : $("#id").val(),
				"password" : password,
				"opType" : type
			},
			success : function(data) {
				if (data.status == 0) {
					alert(data.msg);
					$("input[name='password']").val("")
					$("input[name='confirmpass']").val("")
				} else {
					alert(data.msg);
				}
			},
			error : function(data) {
				alert(data.msg);
			}
		});
	}
}

//检查用户是否存在
function check(obj) {
	if ($.trim($(obj).val()) == "") {
		return;
	}
	$.ajax({
		url : _path + "/agent/check",
		type : "POST",
		datatype : "json",
		data : {
			"username" : $.trim($(obj).val())
		},
		success : function(data) {
			if (data.count == 0) {
				$('#checkAgentTip').addClass('hidden');
				$('#agent_exist_flag').val('0');
			} else {
				$('#checkAgentTip').removeClass('hidden');
				$('#agent_exist_flag').val('1');
			}
		},
		error : function(data) {

		}
	});
}
