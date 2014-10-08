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
<script type="text/javascript" src="${path}/js/teacher.js"></script>
</head>

<body>
	<jsp:include page="../common/header.jsp" />
	<div class="container">
		<div class="row row-offcanvas row-offcanvas-right">
			<jsp:include page="../common/menu.jsp" />
			<div class="col-xs-12 col-sm-10">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">新增教师信息</h3>
					</div>
					<div class="panel-body">
						<div class="col-xs-12" style="height:10px;"></div>
						<div class="tab-content">
							<div class="tab-pane active" id="teacherinfo">
								<form action="${path}/teacher/save" id="teacherForm" method="post" class="form-horizontal">
									<div class="col-xs-12">
										<div class="col-xs-12">
											<div class="form-group">
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>登陆名：</label>
												<div class="col-xs-4">
													<input type="text" name="uname" id="teacherUName" value="${teacher.uname}" maxlength="8" class="form-control borderRadiusIE8 required"  onkeyup="$('#checkTeacherUNameTip').addClass('hidden')">
													<p id="checkTeacherUNameTip" class="help-block hidden"><font color="red"><b>请输入登陆名，2-8个字符</b></font></p>
												</div>
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>教师姓名：</label>
												<div class="col-xs-4">
													<input type="text" name="name" id="teacherName" value="${teacher.name}" maxlength="8" class="form-control borderRadiusIE8 required"  onkeyup="$('#checkTeacherNameTip').addClass('hidden')">
													<p id="checkTeacherNameTip" class="help-block hidden"><font color="red"><b>请输入2-8个汉字</b></font></p>
												</div>
											</div>
											<div class="form-group">
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>身份证号：</label>
												<div class="col-xs-4">
													<input type="text" id="idcard" name="idcard" maxlength="18" value="${teacher.idcard}" class="form-control borderRadiusIE8 required"  onkeyup="$('#checkTeacherTip').addClass('hidden')">
													<p id="checkTeacherTip" class="help-block hidden"><font color="red"><b>该教师已存在！</b></font></p>
													<p id="checkTeacherIdTip" class="help-block hidden"><font color="red"><b>请输入正确的身份证号码</b></font></p>
												</div>
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>联系电话：</label>
												<div class="col-xs-4">
													<input type="text" name="phone" id="teacherTel" value="${teacher.tel}" maxlength="12" class="form-control borderRadiusIE8 required"  onkeyup="$('#checkTeacherTelTip').addClass('hidden')">
													<p id="checkTeacherTelTip" class="help-block hidden"><font color="red"><b>请输入正确的电话或手机号码</b></font></p>
												</div>
											</div>
											<div class="form-group">
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>所属学校：</label>
												<div class="col-xs-4">
													<select id="schoolId" name="schoolId" class="form-control" >
														<c:forEach items="${schools}" var="school">
														<option value="${school.id }">${school.name }</option>
														</c:forEach>
													</select>
												</div>
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>性别：</label>
												<div class="col-xs-4">
													<select id="sex" name="sex" class="form-control" >
														<option value="1" selected>男</option>
														<option value="2">女</option>
													</select>
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
										<button type="submit" class="btn btn-success col-xs-2">完成</button>
										<div class="col-xs-1">&nbsp;</div>
										<button type="button" class="btn btn-default col-xs-2" onclick="goback('新增教师')">取消</button>
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
