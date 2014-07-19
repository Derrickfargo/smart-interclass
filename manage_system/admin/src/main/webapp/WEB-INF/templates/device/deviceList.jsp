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
						<form action="${path}/device" id="searchForm" method="post" class="form-horizontal" >
							<div class="col-xs-12" style="border: 1px solid #f5f5f5;padding: 5px;margin-bottom:5px">
								<div class="form-group">
									<label class="col-xs-1 control-label">IMEI:</label>
									<div class="col-xs-2">
										<input type="text" name="imei" value="${device.imei}" class="form-control borderRadiusIE8">
									</div>
									<label class="col-xs-1 control-label">所属学校:</label>
									<div class="col-xs-2">
										<input type="text" name="schoolId" class="form-control borderRadiusIE8">
									</div>
									<button type="button" class="btn btn-primary" onclick="searchDriver('1')">搜索</button>
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
											<th>所在教室</th>
											<th>所在课桌</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${devices}" var="device">
											<tr>
												<td>${device.imei}</td>
												<td>${device.schoolName}</td>
												<td>${device.roomName}</td>
												<td>${device.tableName}</td>
												<td>
													<a href="#" onclick="modifyDevice('${device.id}')"><span title="修改" class="glyphicon glyphicon-pencil"></span></a>&nbsp; 
													<a href="#" onclick="deleteDevice('${device.id}')"><span title="删除" class="glyphicon glyphicon-remove"></span></a>&nbsp;
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>

							<div class='text-center'>
								<c:choose>
									<c:when test="${empty devices}">
										<b>没有查询到相应的数据</b>
									</c:when>
									<c:when test="${search.totalPage == 1}">
									</c:when>
									<c:otherwise>
										<ul class="pagination" id="pagination">
											<!--
											<li><a href="javascript:void(0);searchDriver('1')">首页</a></li>
											-->

											<c:if test="${search.hasLastPage == true}">
												<li><a href="javascript:void(0);searchDriver('${search.lastPage}')">上一页</a></li>
											</c:if>
											<c:if test="${1<(search.currentPage-3)}">
												<li><a href="javascript:void(0)">...</a></li>
											</c:if>
											<c:forEach var="x" begin="1" end="${search.totalPage}">
												<c:if test="${x>=(search.currentPage-3) && x<=(search.currentPage+3)}">
													<c:choose>
														<c:when test="${x==search.currentPage}">
															<li class="active"><a href="javascript:void(0);searchDriver('${x}')">${x}</a></li>
														</c:when>
														<c:otherwise>
															<li><a href="javascript:void(0);searchDriver('${x}')">${x}</a></li>
														</c:otherwise>
													</c:choose>
												</c:if>
											</c:forEach>
											<c:if test="${(search.totalPage)>(search.currentPage+3)}">
												<li><a href="javascript:void(0)">....</a></li>
											</c:if>
											<c:if test="${search.hasNextPage == true}">
												<li><a href="javascript:void(0);searchDriver('${search.nextPage}')">下一页</a></li>
											</c:if>
											<!--
											<li><a href="javascript:void(0);searchDriver('${search.totalPage}')">尾页</a></li>

											<li>&nbsp;每页 <select name="pageC" onchange="$('#pageSize').val(this.value);searchDriver('1')">
													<option value="1" ${search.pageSize == '1' ? 'selected' : ''}>1</option>
													<option value="5" ${search.pageSize == '5' ? 'selected' : ''}>5</option>
													<option value="10" ${search.pageSize == '10' ? 'selected' : ''}>10</option>
													<option value="15" ${search.pageSize == '15' ? 'selected' : ''}>15</option>
													<option value="20" ${search.pageSize == '20' ? 'selected' : ''}>20</option>
											</select>条
											</li>
											-->
										</ul>
									</c:otherwise>
								</c:choose>
							</div>
								<input type="hidden" id="currentPage" name="currentPage" value="${search.currentPage}"> 
								<input type="hidden" id="pageSize" name="pageSize" value="">
								<input type="hidden" id="opType" name="opType" value="">
								<input type="hidden" id="pageType" name="pageType" value="">
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
