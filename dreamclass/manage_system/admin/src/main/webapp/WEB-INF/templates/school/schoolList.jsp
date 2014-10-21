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
    <script type="text/javascript" src="${path}/js/school.js"></script>
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
						<h3 class="panel-title">学校信息</h3>
					</div>
					<div class="panel-body">
						<form action="${path}/school/list" id="searchForm" method="post" class="form-horizontal" >
							<div class="col-xs-12" style="border: 1px solid #f5f5f5;padding: 5px;margin-bottom:5px">
								<div class="form-group">
									<label class="col-xs-1 control-label">学校名称:</label>
									<div class="col-xs-2">
										<input type="text" name="name" value="${name}" class="form-control borderRadiusIE8">
									</div>
									<label class="col-xs-1 control-label">学校性质:</label>
									<div class="col-xs-2">
										<select id="schoolType" name="schoolType" class="form-control" >
											<c:choose>
												<c:when test="${schoolType == -1}">
													<option value="-1" selected></option>
												</c:when>
												<c:otherwise>
													<option value="-1"></option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${schoolType == 1}">
													<option value="1" selected>小学</option>
												</c:when>
												<c:otherwise>
													<option value="1">小学</option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${schoolType == 2 }">
													<option value="2" selected>中学</option>
												</c:when>
												<c:otherwise>
													<option value="2">中学</option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${schoolType == 3 }">
													<option value="3" selected>其他</option>
												</c:when>
												<c:otherwise>
													<option value="3">其他</option>
												</c:otherwise>
											</c:choose>
										</select>
									</div>
									<button type="button" class="btn btn-primary" onclick="searchSchool('1')">搜索</button>
									<button type="button" class="btn btn-primary" onclick="emptyForm('searchForm')">清空</button>
								</div>
							</div>

                            <div class="col-xs-12" style="padding: 0px;margin-bottom:5px">
								<table class="table table-bordered table-condensed table-hover table-striped">
									<thead>
										<tr class="success">
											<th>学校名称</th>
											<th>学校类型</th>
											<th>联系电话</th>
											<th>Email</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${page.list}" var="school">
											<tr>
												<td>${school.name}</td>
												<td>
													<c:if test="${school.schoolType =='1' }">
														小学
													</c:if>
													<c:if test="${school.schoolType =='2' }">
														中学
													</c:if>
													<c:if test="${school.schoolType =='3' }">
														其他
													</c:if>
												</td>
												<td>${school.phone}</td>
												<td>${school.email}</td>
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
									<c:otherwise>
										<ul class="pagination" id="pagination">
											<li><a href="javascript:void(0);searchSchool('1')">首页</a></li>
											<c:if test="${page.hasPreviousPage}">
												<li><a href="javascript:void(0);searchSchool('${page.prePage}')">上一页</a></li>
											</c:if>
											<c:forEach items="${page.navigatepageNums}" var="nav">
						                        <c:if test="${nav == page.pageNum}">
						                            <li class="active"><a href="javascript:void(0);">${nav}</a></li>
						                        </c:if>
						                        <c:if test="${nav != page.pageNum}">
						                        	<li><a href="javascript:void(0);searchSchool('${nav}')">${nav}</a></li>
						                        </c:if>
						                    </c:forEach>
											<c:if test="${page.hasNextPage}">
												<li><a href="javascript:void(0);searchSchool('${page.nextPage}')">下一页</a></li>
											</c:if>
											<li><a href="javascript:void(0);searchSchool('${page.pages}')">尾页</a></li>
										</ul>
									</c:otherwise>
								</c:choose>
							</div>
							<input type="hidden" id="pageNum" name="pageNum" value="${page.pageNum}"> 
							<input type="hidden" id="pageSize" name="pageSize" value="">
							<input type="hidden" id="opType" name="opType" value="">
							<input type="hidden" id="pageType" name="pageType" value="">
							<input type="hidden" id="schoolId" name="schoolId" value="">
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