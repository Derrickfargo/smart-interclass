<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="zh-cn">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>互动课堂后台管理平台</title>
    <jsp:include page="../common/common.jsp" />
    <script type="text/javascript" src="${path}/js/device.js"></script>
  </head>

  <body>
  	<jsp:include page="../common/header.jsp" />
    <div class="container">
      <div class="row row-offcanvas row-offcanvas-right">
			<jsp:include page="../common/menu.jsp" />
			
<!-- 		begin：-这是 每个页面右边 不同的部分 	 -->
		<!--       <div class="col-xs-12 col-sm-9"> -->
		<!--         <div class="jumbotron"> -->
		<!--           <h1>欢迎您!!</h1> -->
		<!--         </div> -->
		<!--       </div> -->

<!-- 		示例 开始 -->
		<div class="col-xs-12 col-sm-10">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">设备信息</h3>
					</div>
					<div class="panel-body">
						<form action="${path}/device/list" id="searchForm" method="post" class="form-horizontal" >
							<div class="col-xs-12" style="border: 1px solid #f5f5f5;padding: 5px;margin-bottom:5px">
								<div class="form-group">
									<label class="col-xs-1 control-label">IMEI:</label>
									<div class="col-xs-2">
										<input type="text" name="imei" value="${imei}" class="form-control borderRadiusIE8">
									</div>
									<label class="col-xs-1 control-label">所属学校:</label>
									<div class="col-xs-2">
										<input type="text" name="schoolName" value="${schoolName}" class="form-control borderRadiusIE8">
									</div>
									<button type="button" class="btn btn-primary" onclick="searchDevice('1')">搜索</button>
									<button type="button" class="btn btn-primary" onclick="emptyForm('searchForm')">清空</button>
								</div>
							</div>
							<!-- 
							<div class="col-xs-12" style="padding: 0px;margin-bottom:5px">
								<button type="button" class="btn btn-success btn-sm pull-right" onclick="window.location.href='${path}/student/add'">新增</button>
							</div>
 							-->
                            <div class="col-xs-12" style="padding: 0px;margin-bottom:5px">
								<table class="table table-bordered table-condensed table-hover table-striped">
									<thead>
										<tr class="success">
											<th>IMEI</th>
											<th>所属学校</th>
											<th>所在班级</th>
											<th>所属学生</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${page.list}" var="device">
											<tr>
												<td>${device.imei}</td>
												<td>${device.schoolName}</td>
												<td>${device.className}</td>
												<td>${device.studentName}</td>
												<td>
													<a href="#" onclick="deleteDevice('${device.id}')"><span title="删除" class="glyphicon glyphicon-remove"></span></a>&nbsp;
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>

							<div class='text-center'>
								<c:choose>
									<c:when test="${empty page.list}">
										<b>没有查询到相应的数据</b>
									</c:when>
									<c:when test="${page.pages == 1}">
									</c:when>
									<c:otherwise>
										<ul class="pagination" id="pagination">
											<c:if test="${page.pages > 1 }">
												<li><a href="javascript:void(0);searchDevice('1')">首页</a></li>
											</c:if>
											<c:if test="${page.hasPreviousPage}">
												<li><a href="javascript:void(0);searchDevice('${page.prePage}')">上一页</a></li>
											</c:if>
											<c:forEach items="${page.navigatepageNums}" var="nav">
						                        <c:if test="${nav == page.pageNum}">
						                            <li><a href="javascript:void(0);searchDevice('${x}')">${nav}</a></li>
						                        </c:if>
						                        <c:if test="${nav != page.pageNum}">
						                        	<li class="active"><a href="javascript:void(0);searchDevice('${nav}')">${nav}</a></li>
						                        </c:if>
						                    </c:forEach>
											<c:if test="${page.hasNextPage}">
												<li><a href="javascript:void(0);searchDevice('${page.pageSize}')">下一页</a></li>
											</c:if>
											<c:if test="${page.pages > 1 }">
												<li><a href="javascript:void(0);searchDevice('${page.pages}')">尾页</a></li>
											</c:if>
										</ul>
									</c:otherwise>
								</c:choose>
							</div>
							<input type="hidden" id="pageNum" name="pageNum" value="${page.pageNum}"> 
							<input type="hidden" id="pageSize" name="pageSize" value="">
							<input type="hidden" id="opType" name="opType" value="">
							<input type="hidden" id="pageType" name="pageType" value="">
							<input type="hidden" id="deviceId" name="deviceId" value="">
						</form>
					</div>
				</div>
			</div>
<!-- 		示例 结束 -->
<!-- 			end- -这是 每个页面右边 不同的部分 -->
      </div>
      <footer>
         <!--nothing-->
      </footer>
    </div>
  </body>
</html>
