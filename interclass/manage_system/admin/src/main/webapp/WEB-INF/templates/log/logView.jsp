<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
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
<link rel="stylesheet" type="text/css" href="${path}/lib/bootstrap-fileinput/bootstrap-fileinput.css" />
<script type="text/javascript" src="${path}/lib/bootstrap-fileinput/bootstrap-fileinput.js" ></script>
<script type="text/javascript" src="${path}/lib/jquery-validation/jquery.validate.js"></script>
<script type="text/javascript" src="${path}/lib/jquery-validation/jquery.metadata.js"></script>
<script type="text/javascript" src="${path}/lib/jquery-validation/messages_zh.js"></script>
<script type="text/javascript" src="${path}/lib/md5.js"></script>
<script type="text/javascript" src="${path}/js/teacher.js"></script>
</head>

<body>
	<jsp:include page="../common/header.jsp" />
	<script type="text/javascript" src="${path}/js/log.js"></script>
	<div class="container">
		<div class="row row-offcanvas row-offcanvas-right">
			<jsp:include page="../common/menu.jsp" />
			<div class="col-xs-12 col-sm-10">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">查看日志</h3>
					</div>
					<div class="panel-body">
						<div class="col-xs-12" style="height:10px;"></div>
						<div class="tab-content">
							<div class="tab-pane active" id="teacherinfo">
								<form action="${path}/log/view" id="searchForm" method="post" class="form-horizontal">
									<div class="col-xs-12">
										<div class="col-xs-12">
											<div class="form-group">
												<label class="col-xs-2 control-label">日志类型：</label>
												<div class="col-xs-4">
													<c:if test="${log.type =='1' }">
														教师端
													</c:if>
													<c:if test="${log.type =='2' }">
														Pad端
													</c:if>
												</div>
												<label class="col-xs-2 control-label">发生时间：</label>
												<div class="col-xs-4">
													<fmt:formatDate value="${log.ctime}" type="both"/>
												</div>
											</div>
											<div class="form-group">
												<label class="col-xs-2 control-label">错误描述：</label>
												<div class="col-xs-10">
													${log.reason }
												</div>
											</div>
											<div class="form-group">
												<label class="col-xs-2 control-label">日志下载：</label>
												<div class="col-xs-10">
													<a href="#" onclick="downloadLog('${log.id}')">teacher.log<span class="glyphicon glyphicon-save"></span></a>
												</div>
											</div>
											<!-- 
											<div class="form-group text-center help-block">
													提示：XXXX！
											</div>
											 -->
										</div>
									</div>
									<input type="hidden" id="logId" name="logId" value="${log.id }">
									<div class='col-xs-offset-4'>
										<button type="button" class="btn btn-default col-xs-2" onclick="window.history.go(-1);">返回</button>
									</div>
								</form>
							</div>
						</div><!-- tab-content end -->


						</div><!-- panel-body end -->
					</div><!-- panel end -->


				</div><!-- col-sm-10 end -->

			</div><!-- row end -->
			<footer>
				<!--nothing-->
			</footer>
		</div><!-- container end -->

</body>
</html>
