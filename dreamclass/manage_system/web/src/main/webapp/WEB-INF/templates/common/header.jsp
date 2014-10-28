<%@ page language="java" pageEncoding="UTF-8"%>
<div class="navbar navbar-fixed-top navbar-inverse" role="navigation">
<div class="container">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
          <span class="sr-only">Toggle navigation</span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="#">页面开发演示</a>
      </div>
      <div class="navbar-collapse collapse">
        <ul class="nav navbar-nav">
          <li class="active"><a href="#">首页</a></li>
          <li><a href="#about">动态</a></li>
          <li><a href="#contact">日志</a></li>
          <li><a href="#contact">相册</a></li>
          <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">更多 <b class="caret"></b></a>
            <ul class="dropdown-menu">
              <li><a href="#">百科</a></li>
              <li><a href="#">积分商城</a></li>
              <li class="divider"></li>
              <li class="dropdown-header">其他</li>
              <li><a href="#">百度</a></li>
              <li><a href="#">谷歌</a></li>
            </ul>
          </li>
        </ul>
 				<form class="navbar-form navbar-right" role="form">
          <div class="form-group">
            <input type="text" placeholder="Email" class="form-control">
          </div>
          <div class="form-group">
            <input type="password" placeholder="Password" class="form-control">
          </div>
          <button type="submit" class="btn btn-success">Sign in</button>
        </form>
      </div><!--/.nav-collapse -->
    </div>
</div><!-- /.navbar -->