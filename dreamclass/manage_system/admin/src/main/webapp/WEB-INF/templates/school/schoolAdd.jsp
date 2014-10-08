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
<script type="text/javascript" src="${path}/js/school.js"></script>
</head>

<body>
	<jsp:include page="../common/header.jsp" />
	<div class="container">
		<div class="row row-offcanvas row-offcanvas-right">
			<jsp:include page="../common/menu.jsp" />
			<div class="col-xs-12 col-sm-10">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">新增学校</h3>
					</div>
					<div class="panel-body">
						<div class="col-xs-12" style="height:10px;"></div>
						<div class="tab-content">
							<div class="tab-pane active" id="schoolinfo">
								<form action="${path}/school/save" id="schoolForm" method="post" class="form-horizontal">
									<div class="col-xs-12">
										<div class="col-xs-12">
											<div class="form-group">
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>学校名称：</label>
												<div class="col-xs-4">
													<input type="text" name="name" id="schoolName" value="${school.name}" maxlength="128" class="form-control borderRadiusIE8 required"  onkeyup="$('#checkSchoolNameTip').addClass('hidden')">
													<p id="checkSchoolNameTip" class="help-block hidden"><font color="red"><b>请输入学校名称</b></font></p>
												</div>
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>邮编：</label>
												<div class="col-xs-4">
													<input type="text" name="zipCode" id="zipCode" value="${school.zipCode}" maxlength="8" class="form-control borderRadiusIE8 required"  onkeyup="$('#checkZipCodeTip').addClass('hidden')">
													<p id="checkZipCodeTip" class="help-block hidden"><font color="red"><b>请输入正确的 邮编</b></font></p>
												</div>
											</div>
											<div class="form-group">
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>邮箱：</label>
												<div class="col-xs-4">
													<input type="text" id="email" name="email" maxlength="18" value="${school.email}" class="form-control borderRadiusIE8 required"  onkeyup="$('#checkEmailTip').addClass('hidden')">
													<p id="checkEmailTip" class="help-block hidden"><font color="red"><b>请输入正确的 邮箱</b></font></p>
												</div>
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>联系电话：</label>
												<div class="col-xs-4">
													<input type="text" name="phone" id="phone" value="${school.phone}" maxlength="12" class="form-control borderRadiusIE8 required"  onkeyup="$('#checkPhoneTip').addClass('hidden')">
													<p id="checkPhoneTip" class="help-block hidden"><font color="red"><b>请输入正确的电话或手机号码</b></font></p>
												</div>
											</div>
											<div class="form-group">
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>通讯地址：</label>
												<div class="col-xs-4">
													<input type="text" id="address" name="address" value="${school.schoolName}" class="form-control borderRadiusIE8 required" onkeyup="$('#checkAddressTip').addClass('hidden')">
													<p id="checkAddressTip" class="help-block hidden"><font color="red"><b>请输入所属学校</b></font></p>
												</div>
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>学校性质：</label>
												<div class="col-xs-4">
													<select id="schoolType" name="schoolType" class="form-control" >
														<option value="1" selected>小学</option>
														<option value="2">中学</option>
														<option value="3">其他</option>
													</select>
												</div>
											</div>
											<div class="form-group">
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>办学性质：</label>
												<div class="col-xs-4">
													<select id="educationalType" name="educationalType" class="form-control" >
														<option value="1" selected>民办</option>
														<option value="2">公办</option>
														<option value="3">民办公助</option>
														<option value="4">私立学校</option>
														<option value="5">其他</option>
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
										<button type="button" class="btn btn-default col-xs-2" onclick="goback('新增学校')">取消</button>
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
