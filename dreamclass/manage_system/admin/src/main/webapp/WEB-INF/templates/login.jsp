<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!doctype html>
<html data-ng-app="logistics">
<head>
<base href="${path}">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>互动课堂后台管理平台</title> 
<jsp:include page="common/common.jsp"></jsp:include>
<script src="${path}/lib/md5.js"></script>
<link rel="stylesheet" href="${path}/lib/bootstrap/css/signin.css" />
<script type="text/javascript">

$(document).ready(function(){
	var msg ="${msg}";
	if(msg=="1"){
		$("#p").html('<font color="red">用户名或密码不正确</font>');
	}else if(msg=="2"){
		$("#p").html('<font color="red">用户被注销，请联系管理员进行激活</font>');
	}
	
});

function enter_(){
	var result=false;
	if($.trim($("#user").val())==""){
		$("#u").html('<font color="red">用户名不能为空</font>');
		 
	}else{
		$("#u").html('');
		result=true;
	}
	if($.trim($(":password").val())==""){
		$("#p").html('<font color="red">密码不能为空</font>');
		return false;
	}else{
		$("#p").html('');
		result=true;
	}
	
	if(result){
		var original_pass = $("input[name='passwd']").val();
		$("input[name='passwd']").val(hex_md5(original_pass));
	}
	return result;
}


function enterU(){
	if($.trim($("#user").val())==""){
		$("#u").html('<font color="red">用户名不能为空</font>');
	}else{
		$("#u").html('');
	}
}

function enterP(){
	if($.trim($(":password").val())==""){
		$("#p").html('<font color="red">密码不能为空</font>');
	}else{
		$("#p").html('');
	}
}
</script>
</head>

<body>
<jsp:include page="common/header.jsp" />
    <div class="container">

      <form class="form-signin" action="${path}/login" role="form" onsubmit="return enter_();" method="post">
        <h2 class="form-signin-heading">请登录</h2>
        <input type="text" id="user" name ="uname" value="${user.uname}" class="form-control" placeholder="用户名" onblur="enterU()"  autofocus>
        <label id="u" > </label>
        <input type="password" name="password"  class="form-control" placeholder="密码" onblur="enterP()" >
        <label id="p" > </label>
<!--         <label class="checkbox"> -->
<!--           <input type="checkbox" value="remember-me"> 自动登录 -->
<!--         </label> -->
        <button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
      </form>

    </div>
</body>

</html>
