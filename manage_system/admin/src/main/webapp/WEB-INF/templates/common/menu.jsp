<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="col-xs-6 col-sm-2 sidebar-offcanvas" id="sidebar" role="navigation">
	<div id="accordion">
		<div class="panel panel-default bgnone">
		  	<a class="list-group-item active">学校信息管理</a>
		  	<ul class="nav">
		    	<li class=""><a href="${path}/school/list" > 学校信息</a></li>
		    	<li class=""><a href="${path}/room/list" > 教室信息</a></li>
		  	</ul>
		  	<a href="#" class="list-group-item active"> 教师信息管理</a>
			<ul class="nav">
		    	<li class=""><a href="${path}/teacher/list" > 教师信息</a></li>
		  	</ul>
		  	<a href="#" class="list-group-item active">课程信息管理</a>
			<ul class="nav">
		    	<li class=""><a href="${path}/course/list" > 课程信息</a></li>
		  	</ul>
 			<a class="list-group-item active">设备信息管理</a>  
 			<ul class="nav">
		    	<li class=""><a href="${path}/device/list" > 设备信息</a></li>
		  	</ul>
			<c:if test="${sessionScope.user.uname=='admin'}">
			<a href="#" class="list-group-item">系统管理</a>
			<ul class="nav">
				<li class=""><a href="${path}/user/list" > 用户管理</a></li>
		    </ul>
		   </c:if>
		</div>
	</div>
</div>
