<%@ page language="java" pageEncoding="UTF-8"%>
<%
  String path = request.getContextPath();
  request.setAttribute("path", path);
%>
<!--     <link rel="shortcut icon" href="../../docs-assets/ico/favicon.png"> -->
<!-- 公共资源 -->
<!-- Bootstrap core CSS -->
<link href="${path}/lib/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<link href="${path}/lib/bootstrap-datetimepicker/bootstrap-datetimepicker.css" rel="stylesheet"/>
<!-- Custom styles for this template -->
<link href="${path}/css/offcanvas.css" rel="stylesheet">
<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
  <script src="${path}/lib/html5shiv.min.js"></script>
  <script src="${path}/lib/respond.min.js"></script>
<![endif]-->

<script>var _path = '${path}';</script>
<script src="${path}/lib/jquery.min.js"></script>
<script src="${path}/lib/bootstrap/js/bootstrap.js"></script>
