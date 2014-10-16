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
<script type="text/javascript" src="${path}/js/version.js"></script>
</head>

<body>
	<jsp:include page="../common/header.jsp" />
	<div class="container">
		<div class="row row-offcanvas row-offcanvas-right">
			<jsp:include page="../common/menu.jsp" />
			<div class="col-xs-12 col-sm-10">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">新增版本信息</h3>
					</div>
					<div class="panel-body">
						<div class="col-xs-12" style="height:10px;"></div>
						<div class="tab-content">
							<div class="tab-pane active" id="teacherinfo">
								<form action="${path}/version/save" enctype="multipart/form-data" id="versionForm" method="post" class="form-horizontal">
									<div class="col-xs-12">
										<div class="col-xs-12">
											<div class="form-group">
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>版本类型：</label>
												<div class="col-xs-4">
													<select id="type" name="type" class="form-control" >
														<option value="1" selected>教师端</option>
														<option value="2">学生端</option>
													</select>
												</div>
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>版本号：</label>
												<div class="col-xs-4">
													<input type="text" name="name" id="name" value="${version.name}" maxlength="32" class="form-control borderRadiusIE8 required"  onkeyup="$('#checkNameTip').addClass('hidden')">
													<p id="checkNameTip" class="help-block hidden"><font color="red"><b>请输入版本号</b></font></p>
												</div>
											</div>
											<div class="form-group">
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>版本代码：</label>
												<div class="col-xs-4">
													<input type="text" name="code" id="code" value="${version.code}" maxlength="8" class="form-control borderRadiusIE8 required"  onkeyup="$('#checkCodeTip').addClass('hidden')">
													<p id="checkCodeTip" class="help-block hidden"><font color="red"><b>请输入2-8个汉字</b></font></p>
												</div>
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>版本文件：</label>
												<div class="col-xs-4">
													<input type="file" id="file" name="file" class="form-control borderRadiusIE8 required" onkeyup="$('#fileTip').addClass('hidden')"/>
													<p id="fileTip" class="help-block hidden"><font color="red"><b>请选择要导入的Excel文件</b></font></p>
												</div>
											</div>
											<div class="form-group">
												<label class="col-xs-2 control-label"><span class="span-red-bold">* </span>版本描述：</label>
												<div class="col-xs-10">
													<textarea name="description" id="description" class="col-xs-12" rows="5" cols="10"></textarea>
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
										<button type="button" class="btn btn-default col-xs-2" onclick="goback('新增版本')">取消</button>
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
