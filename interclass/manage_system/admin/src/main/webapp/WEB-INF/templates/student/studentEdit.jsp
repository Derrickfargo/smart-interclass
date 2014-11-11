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
						<h3 class="panel-title">修改学生信息</h3>
					</div>
					<div class="panel-body">
						<div class="col-xs-12" style="height:10px;"></div>
						<div class="tab-content">
							<div class="tab-pane active" id="schoolinfo">
								<form action="${path}/student/update" id="studentForm" method="post" class="form-horizontal">
									<div class="col-xs-12">
										<div class="col-xs-12">
											<div class="form-group">
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>学校：</label>
												<div class="col-xs-4">
												
													<select id="schoolId" name="schoolId" class="form-control" >
														<c:forEach items="${schools}" var="school">
														<c:if test="${school.id==student.schoolId}">
														<option value="${school.id}" selected>${school.name }</option>
														</c:if>
														<c:if test="${school.id!=student.schoolId}">
														<option value="${school.id}">${school.name }</option>
														</c:if>
													
														</c:forEach>
													</select>
												</div>
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>年级：</label>
												<div class="col-xs-4">
													<select id="year" name="year" class="form-control" >
														<option value="1" <c:if test="${year==1}">selected</c:if>>1年级</option>
														<option value="2" <c:if test="${year==2}">selected</c:if>>2年级</option>
														<option value="3" <c:if test="${year==3}">selected</c:if>>3年级</option>
														<option value="4" <c:if test="${year==4}">selected</c:if>>4年级</option>
														<option value="5" <c:if test="${year==5}">selected</c:if>>5年级</option>
														<option value="6" <c:if test="${year==6}">selected</c:if>>6年级</option>
														<option value="7" <c:if test="${year==7}">selected</c:if>>7年级</option>
														<option value="8" <c:if test="${year==8}">selected</c:if>>8年级</option>
														<option value="9" <c:if test="${year==9}">selected</c:if>>9年级</option>
													</select>
												</div>
											</div>
											<div class="form-group">
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>班级：</label>
												<div class="col-xs-4">
													<input type="text" id="classNumber" name="classNumber" maxlength="3" class="form-control borderRadiusIE8 required" value="${student.classNumber}" onkeyup="$('#checkClassNumberTip').addClass('hidden')">
													<p id="checkClassNumberTip" class="help-block hidden"><font color="red"><b>请输入班级</b></font></p>
												</div>
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>姓名：</label>
												<div class="col-xs-4">
													<input type="text" id="name" name="name" maxlength="18" class="form-control borderRadiusIE8 required" value="${student.name}" onkeyup="$('#checkNameTip').addClass('hidden')">
													<p id="checkNameTip" class="help-block hidden"><font color="red"><b>请输入姓名</b></font></p>
												</div>
											</div>
											<div class="form-group">
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>学号：</label>
												<div class="col-xs-4">
													<input type="text" id="number" name="number" maxlength="18" class="form-control borderRadiusIE8 required" value="${student.number}" onkeyup="$('#checkNumberTip').addClass('hidden')">
													<p id="checkNumberTip" class="help-block hidden"><font color="red"><b>请输入学号</b></font></p>
												</div>
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>性别：</label>
												<div class="col-xs-4">
													<select id="sex" name="sex" class="form-control" >
														<option value="1" <c:if test="${student.sex==1}">selected</c:if>>男</option>
														<option value="2" <c:if test="${student.sex==2}">selected</c:if>>女</option>
													</select>
												</div>
											</div>
											<div class="form-group">
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>监护人：</label>
												<div class="col-xs-4">
													<input type="text" name="guardian" id="guardian" class="form-control borderRadiusIE8 required" value="${student.guardian}" onkeyup="$('#checkGuardianTip').addClass('hidden')">
													<p id="checkGuardianTip" class="help-block hidden"><font color="red"><b>请输入正确的监护人</b></font></p>
												</div>
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>联系电话：</label>
												<div class="col-xs-4">
													<input type="text" name="phone" id="phone" maxlength="12" class="form-control borderRadiusIE8 required" value="${student.phone}" onkeyup="$('#checkPhoneTip').addClass('hidden')">
													<p id="checkPhoneTip" class="help-block hidden"><font color="red"><b>请输入正确的电话或手机号码</b></font></p>
												</div>
											</div>
											<div class="form-group">
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>通讯地址：</label>
												<div class="col-xs-4">
													<input type="text" id="address" name="address" class="form-control borderRadiusIE8 required" value="${student.address}" onkeyup="$('#checkAddressTip').addClass('hidden')">
													<p id="checkAddressTip" class="help-block hidden"><font color="red"><b>请输入所属学校</b></font></p>
												</div>
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>Pad Mac地址：</label>
												<div class="col-xs-4">
													<input type="text" name="imei" id="imei" maxlength="36" class="form-control borderRadiusIE8 required" value="${student.imei}" onkeyup="$('#checkIMEITip').addClass('hidden')" autocomplete="off">
													<p id="checkIMEITip" class="help-block hidden"><font color="red"><b>Pad地址</b></font></p>
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
										<button type="button" class="btn btn-default col-xs-2" onclick="goback('修改学生信息')">取消</button>
									</div>
									<input type="hidden" name="deviceId" value="${student.deviceId}"/>
									<input type="hidden" id="studentId" name="id" value="${student.id }"/>
									<input type="hidden" name="classId" value="${student.classId}"/>
									<input type="hidden" name="avatar"  value="${student.avatar}"/>
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
