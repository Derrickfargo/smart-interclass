<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="navbar navbar-fixed-top navbar-inverse" role="navigation">
  <div class="container">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="#">互动课堂后台管理平台</a>
    </div>
    <c:if test="${sessionScope.user!=null}">
  <div class="collapse navbar-collapse"  style="float:right">
       <ul class="nav " id="quit">
       <li class="active"><a  href="${path}/userInfo/quit"">退出</a></li> 
       </ul> 
 </div>
 </c:if>
  </div><!-- /.container -->
</div><!-- /.navbar -->
