//页面初始化设置
$(document).ready(function() {
	$("#autocompleteInput").autocomplete({
        source:function(query,process){
            var matchCount = 10;//返回结果集最大数量
            $.post("/admin/room/search",{"name":query,"pageNum":matchCount},function(respData){
             
            	return process(respData);
            });
        },
        formatItem:function(item){
        	return item["name"];
        },
        setValue:function(item){
            return {"data-value":item["name"],"real-value":item["name"]};
        }
    });
 
	$("#goBtn").click(function(){ //获取文本框的实际值
        var regionCode = $("#autocompleteInput").attr("real-value") || "";
        alert(regionCode);
    });
	
	$("#roomForm").validate({
		submitHandler:function(form){
			var flag = true;
			//学校名称
//			if (!new RegExp(/^[\u4e00-\u9fa5]{2,8}$/).test($("#roomName").val())) {
//				$("#checkRoomNameTip").removeClass("hidden");
//				flag = false;
//			}
//			//检查身份证格式是否正确
//			var idCarNo = new RegExp(/^\d{17}([0-9]|X)$|^(\d{15})$/);
//			if (!idCarNo.test($("#idcard").val())) {
//				$("#checkRoomIdTip").removeClass("hidden");
//				flag = false;
//			}
//			//检查联系方式格式是否正确
//			var isTelNum = new RegExp(/^\d{11}/);
//			if (!isTelNum.test($("#roomTel").val())) {
//				$("#checkRoomTelTip").removeClass("hidden");
//				flag = false;
//			}
//
//			if ($("#roomName").val() == "") {
//				$('#checkRoomRoomTip').removeClass('hidden');
//				flag = false;
//			}
			
			if (flag) {
				form.submit();
			}
		}
	});
});


// 搜索教师信息
function searchRoom(pageNum) {
	$("#pageNum").val(pageNum);
	$("#searchForm").attr("action", _path + "/room/list");
	$("#searchForm").submit();
}

// 页面跳转，查看教师详情或者修改教师信息
function modifyRoom(roomId) {
	$("#roomId").val(roomId);
	$("#searchForm").attr("action", _path + "/room/edit");
	$("#searchForm").submit();
}


function deleteRoom(roomId){
	if(confirm("确认要删除本条记录吗?")){
		$("#roomId").val(roomId);
		$("#searchForm").attr("action", _path + "/room/delete");
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
function saveRoom() {
	$("#roomForm").submit();
}

