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
<script type="text/javascript" src="${path}/js/student.js"></script>
</head>

<body>
	<jsp:include page="../common/header.jsp" />
	<div class="container">
		<div class="row row-offcanvas row-offcanvas-right">
			<jsp:include page="../common/menu.jsp" />
			<div class="col-xs-12 col-sm-10">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">新增学生</h3>
					</div>
					<div class="panel-body">
						<div class="col-xs-12" style="height:10px;"></div>
						<div class="tab-content">
							<div class="tab-pane active" id="schoolinfo">
								<form action="${path}/student/save" id="schoolForm" method="post" class="form-horizontal">
									<div class="col-xs-12">
										<div class="col-xs-12">
											<div class="form-group">
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>学校：</label>
												<div class="col-xs-4">
													<select id="schoolId" name="schoolId" class="form-control" >
														<c:forEach items="${schools}" var="school">
														<option value="${school.id }">${school.name }</option>
														</c:forEach>
													</select>
												</div>
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>年级：</label>
												<div class="col-xs-4">
													<select id="year" name="year" class="form-control" >
														<option value="1">1年级</option>
														<option value="2">2年级</option>
														<option value="3">3年级</option>
														<option value="4">4年级</option>
														<option value="5">5年级</option>
														<option value="6">6年级</option>
														<option value="7">7年级</option>
														<option value="8">8年级</option>
														<option value="9">9年级</option>
													</select>
												</div>
											</div>
											<div class="form-group">
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>班级：</label>
												<div class="col-xs-4">
													<input type="text" id="classNumber" name="classNumber" maxlength="2" class="form-control borderRadiusIE8 required"  onkeyup="$('#checkClassNumberTip').addClass('hidden')">
													<p id="checkClassNumberTip" class="help-block hidden"><font color="red"><b>请输入班级</b></font></p>
												</div>
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>姓名：</label>
												<div class="col-xs-4">
													<input type="text" id="name" name="name" maxlength="18" class="form-control borderRadiusIE8 required"  onkeyup="$('#checkNameTip').addClass('hidden')">
													<p id="checkNameTip" class="help-block hidden"><font color="red"><b>请输入姓名</b></font></p>
												</div>
											</div>
											<div class="form-group">
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>学号：</label>
												<div class="col-xs-4">
													<input type="text" id="number" name="number" maxlength="18" class="form-control borderRadiusIE8 required"  onkeyup="$('#checkNumberTip').addClass('hidden')">
													<p id="checkNumberTip" class="help-block hidden"><font color="red"><b>请输入学号</b></font></p>
												</div>
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>性别：</label>
												<div class="col-xs-4">
													<select id="sex" name="sex" class="form-control" >
														<option value="1" selected>男</option>
														<option value="2">女</option>
													</select>
												</div>
											</div>
											<div class="form-group">
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>监护人：</label>
												<div class="col-xs-4">
													<input type="text" name="guardian" id="guardian" class="form-control borderRadiusIE8 required"  onkeyup="$('#checkGuardianTip').addClass('hidden')">
													<p id="checkGuardianTip" class="help-block hidden"><font color="red"><b>请输入正确的监护人</b></font></p>
												</div>
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>联系电话：</label>
												<div class="col-xs-4">
													<input type="text" name="phone" id="phone" maxlength="12" class="form-control borderRadiusIE8 required"  onkeyup="$('#checkPhoneTip').addClass('hidden')">
													<p id="checkPhoneTip" class="help-block hidden"><font color="red"><b>请输入正确的电话或手机号码</b></font></p>
												</div>
											</div>
											<div class="form-group">
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>通讯地址：</label>
												<div class="col-xs-4">
													<input type="text" id="address" name="address" class="form-control borderRadiusIE8 required" onkeyup="$('#checkAddressTip').addClass('hidden')">
													<p id="checkAddressTip" class="help-block hidden"><font color="red"><b>请输入所属学校</b></font></p>
												</div>
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>Pad设备号：</label>
												<div class="col-xs-4">
													<input type="text" name="imei" id="imei" maxlength="36" class="form-control borderRadiusIE8 required"  onkeyup="$('#checkIMEITip').addClass('hidden')">
													<p id="checkIMEITip" class="help-block hidden"><font color="red"><b>Pad设备号</b></font></p>
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
										<button type="button" class="btn btn-default col-xs-2" onclick="goback('新增学生')">取消</button>
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
