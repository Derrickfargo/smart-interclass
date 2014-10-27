// 搜索
function searchDevice(pageNum) {
	$("#pageNum").val(pageNum);
	$("#searchForm").attr("action", _path + "/device/list");
	$("#searchForm").submit();
}

function deleteDevice(deviceId){
	if(confirm("确认要删除本条记录吗?")){
		$("#deviceId").val(deviceId);
		$("#searchForm").attr("action", _path + "/device/delete");
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
