<%@ page language="java" pageEncoding="UTF-8"%>
<div class="col-xs-6 col-sm-3 sidebar-offcanvas" id="sidebar" role="navigation">
   
	<div id="accordion">
		<div class="panel panel-default bgnone">
		
			<a href="#" class="list-group-item active">人员信息管理</a>
			<ul class="nav">
		    <li class=""><a href="${path}/driver" ><span class="glyphicon glyphicon-screenshot"></span> 司机信息</a></li>
				<li class=""><a href="${path}/agent" ><span class="glyphicon glyphicon-tower"></span> 货代信息</a></li>
		  </ul>
			<a href="${path}/goods" class="list-group-item">货源信息管理</a>
			<a href="${path}/order/list" class="list-group-item">订单信息管理</a>
			<a href="${path}/monitor.jsp" class="list-group-item">载途监控管理</a>
			<a href="${path}/analysis" class="list-group-item">数据统计管理</a>
			<a href="${path}/noticePublish.jsp" class="list-group-item">公共信息管理</a>
			<a href="#" class="list-group-item">系统管理</a>
			<ul class="nav">
				<li class=""><a href="${path}/userInfo" ><span class="glyphicon glyphicon-user"></span> 用户管理</a></li>
		  </ul>
		  
		</div>
		
	</div>
</div>
