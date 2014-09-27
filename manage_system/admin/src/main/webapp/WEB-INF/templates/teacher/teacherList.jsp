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
    <script type="text/javascript" src="${path}/js/teacher.js"></script>
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
						<h3 class="panel-title">教师信息</h3>
					</div>
					<div class="panel-body">
						<form action="${path}/driver" id="searchForm" method="post" class="form-horizontal" >
							<div class="col-xs-12" style="border: 1px solid #f5f5f5;padding: 5px;margin-bottom:5px">
								<div class="form-group">
									<label class="col-xs-1 control-label">姓名:</label>
									<div class="col-xs-2">
										<input type="text" name="name" value="${teacher.name}" class="form-control borderRadiusIE8">
									</div>
									<label class="col-xs-1 control-label">学校:</label>
									<div class="col-xs-2">
										<select name="cancelFlag" class="form-control">
											<option value="0">全部</option>
										</select>
									</div>
									<button type="button" class="btn btn-primary" onclick="searchDriver('1')">搜索</button>
									<button type="button" class="btn btn-primary" onclick="emptyForm('searchForm')">清空</button>
								</div>
							</div>

							<div class="col-xs-12" style="padding: 0px;margin-bottom:5px">
								<button type="button" class="btn btn-success btn-sm pull-right" onclick="window.location.href='${path}/teacher/add'">新增</button>
							</div>

                            <div class="col-xs-12" style="padding: 0px;margin-bottom:5px">
								<table class="table table-bordered table-condensed table-hover table-striped">
									<thead>
										<tr class="success">
											<th>教师姓名</th>
											<th>所属学校</th>
											<th>联系电话</th>
											<th>性别</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${page.list}" var="teacher">
											<tr>
												<td>${teacher.name}</td>
												<td>${teacher.schoolName}</td>
												<td>${teacher.phone}</td>
												<td>${teacher.sex == '1' ? '男' : '女'}</td>
												<td>
													<a href="#" onclick="modifyTeacher('${teacher.id}')"><span title="修改" class="glyphicon glyphicon-pencil"></span></a>&nbsp; 
													<a href="#" onclick="deleteTeacher('${teacher.id}')"><span title="删除" class="glyphicon glyphicon-remove"></span></a>
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
												<li><a href="javascript:void(0);searchDriver('1')">首页</a></li>
											</c:if>
											<c:if test="${page.hasPreviousPage}">
												<li><a href="javascript:void(0);searchDriver('${search.lastPage}')">上一页</a></li>
											</c:if>
											<c:forEach items="${page.navigatepageNums}" var="nav">
						                        <c:if test="${nav == page.pageNum}">
						                            <li><a href="javascript:void(0);searchDriver('${x}')">${nav}</a></li>
						                        </c:if>
						                        <c:if test="${nav != page.pageNum}">
						                        	<li class="active"><a href="javascript:void(0);searchDriver('${nav}')">${nav}</a></li>
						                        </c:if>
						                    </c:forEach>
											<c:if test="${page.hasNextPage}">
												<li><a href="javascript:void(0);searchDriver('${search.nextPage}')">下一页</a></li>
											</c:if>
											<c:if test="${page.pages > 1 }">
												<li><a href="javascript:void(0);searchDriver('${search.totalPage}')">尾页</a></li>
											</c:if>
										</ul>
									</c:otherwise>
								</c:choose>
							</div>
								<input type="hidden" id="currentPage" name="currentPage" value="${search.currentPage}"> 
								<input type="hidden" id="pageSize" name="pageSize" value="">
								<input type="hidden" id="driverId" name="driverId" value="">
								<input type="hidden" id="opType" name="opType" value="">
								<input type="hidden" id="teacherId" name="teacherId" value="">
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
