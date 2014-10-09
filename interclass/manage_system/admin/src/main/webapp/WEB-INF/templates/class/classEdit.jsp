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
<link rel="stylesheet" type="text/css" href="${path}/lib/bootstrap-fileinput/bootstrap-fileinput.css" />
<script type="text/javascript" src="${path}/lib/bootstrap-fileinput/bootstrap-fileinput.js" ></script>
<script type="text/javascript" src="${path}/lib/jquery-validation/jquery.validate.js"></script>
<script type="text/javascript" src="${path}/lib/jquery-validation/jquery.metadata.js"></script>
<script type="text/javascript" src="${path}/lib/jquery-validation/messages_zh.js"></script>
<script type="text/javascript" src="${path}/lib/md5.js"></script>
<script type="text/javascript" src="${path}/js/class.js"></script>
</head>

<body>
	<jsp:include page="../common/header.jsp" />
	<div class="container">
		<div class="row row-offcanvas row-offcanvas-right">
			<jsp:include page="../common/menu.jsp" />
			<div class="col-xs-12 col-sm-10">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">修改班级</h3>
					</div>
					<div class="panel-body">
						<div class="col-xs-12" style="height:10px;"></div>
						<div class="tab-content">
							<div class="tab-pane active" id="classinfo">
								<form action="${path}/class/update" id="classForm" method="post" class="form-horizontal">
									<div class="col-xs-12">
										<div class="col-xs-12">
											<div class="form-group">
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>班级名称：</label>
												<div class="col-xs-4">
													<input type="text" name="name" id="className" value="${classes.name}" maxlength="8" class="form-control borderRadiusIE8 required"  onkeyup="$('#checkClassNameTip').addClass('hidden')">
													<p id="checkClassNameTip" class="help-block hidden"><font color="red"><b>请输入班级名称</b></font></p>
												</div>
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>所属学校：</label>
												<div class="col-xs-4">
													<select id="schoolId" name="schoolId" class="form-control" >
														<c:forEach items="${schools}" var="school">
														<c:choose>
															<c:when test="${school.id == classes.schoolId}">
																<option value="${school.id }" selected>${school.name }</option>
															</c:when>
															<c:otherwise>
																<option value="${school.id }">${school.name }</option>
															</c:otherwise>
														</c:choose>
														</c:forEach>
													</select>
												</div>
											</div>
											<div class="form-group">
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>入学年份：</label>
												<div class="col-xs-4">
													<input type="text" name="year" id="year" value="${classes.year}" maxlength="8" class="form-control borderRadiusIE8 required"  onkeyup="$('#checkYearTip').addClass('hidden')">
													<p id="checkYearTip" class="help-block hidden"><font color="red"><b>请输入入学年份</b></font></p>
												</div>
											</div>
											<!-- 
											<div class="form-group text-center help-block">
													提示：XXXX！
											</div>
											 -->
										</div>
									</div>
									
									<div class='col-xs-offset-4'>
										<input type="hidden" id="id" name="id" value="${classes.id }">
										<button type="submit" class="btn btn-success col-xs-2">完成</button>
										<div class="col-xs-1">&nbsp;</div>
										<button type="button" class="btn btn-default col-xs-2" onclick="goback('修改班级')">取消</button>
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
