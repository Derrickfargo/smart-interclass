/**
 * Copyright (c) 2010 S9,Inc.All rights reserved.
 * Created by 2010-7-16 
 */
package com.incito.interclass.common;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 系统资源权限过滤器
 * 
 * @author: zhaiguangtao
 * @data: 2013-7-1
 */
public class WebSecurityFilter extends SecurityFilter {

  /**
   * 权限过滤
   */
  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;

    String uri = req.getRequestURI();
    System.out.println(uri);

    // 1，白名单判断
    if (!uri.matches(super.urls)) {

      // 2，登录判断
      Object so = req.getSession().getAttribute("webuser");
      if (so == null || so.equals("")) {
        super.forword(req, res, req.getContextPath() + "/index?type=1");
        return;
      } else {
        // 此处只针对服务操作，排除jsp资源考虑在拦截器中做权限处理
        // 通过组件uri前缀 和 web页面组件目录名来做组件权限判断，例如 /user/modfiyPwd ,/s9/user
        // 3，角色权限判断
        // User user =(User)so;
        // List<String> source = user.getResourceList();
        // if(!source.contains(uri)){// 如果当前用户没有访问此资源的权限
        // forword(res,"/error/noSecurity.html");
        // return;
        // }
      }
    }
    chain.doFilter(request, response);
  }

}
