/**
 * 设置未来(全局)的AJAX请求默认选项 主要设置了AJAX请求遇到Session过期的情况
 */
$.ajaxSetup({
	type : 'POST',
	complete : function(xhr, status) {
		var sessionStatus = xhr.getResponseHeader('sessionstatus');
		if (sessionStatus == 'timeout') {
			//var top = getTopWinow();
			/*
			var yes = confirm('请登录后再操作!');
			if (yes) {
				$('#loginModal').modal('show');
				//top.location.href = _path + '/login.jsp';
			}
			*/
			$('#loginModal').modal('show');
		}
	}
});

/**
 * 在页面中任何嵌套层次的窗口中获取顶层窗口
 * 
 * @return 当前页面的顶层窗口对象
 */
function getTopWinow() {
	var p = window;
	while (p != p.parent) {
		p = p.parent;
	}
	return p;
}

//在各自页面body 调用 添加导航选中 根据导航的位置 从左到右 ，从0开始 传入参数
function setnav(i){
	$("#navbar-nav li").eq(i).find("a").attr("class","menu-link mActive");
}

/**
 * 替换空字符的情况
 * @param str
 * @returns
 */
function isNull(str){
	return str == undefined ? '':str;
}
 
/**
 * 获取url后面的参数
 * */
function GetQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}
