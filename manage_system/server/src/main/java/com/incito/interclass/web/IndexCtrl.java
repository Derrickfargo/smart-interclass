package com.incito.interclass.web;

import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.incito.interclass.common.BaseCtrl;

/**
 * web 首页 业务处理
 * @author zhaigt
 *
 */
@RestController
@RequestMapping("/")
public class IndexCtrl extends BaseCtrl{
  
  /**
   * web端 首页 数据处理
   * 接收web首页 根路径的请求，以及/index 的请求 
   * 改访问绑定两个路径 便于访问 和程序跳转处理
   */
  @RequestMapping(value = {"","/index"})
  public ModelAndView index(){
    ModelAndView mv = new ModelAndView("index");// 同步跳转
    try {
      mv.addObject("msg", "hello");// 绑定变量
    } catch (Exception e) {
      e.printStackTrace();
    }
    return mv;
  }
      
  /**
   * 用户退出登录
   */
  @RequestMapping("/quit")
  public ModelAndView quit(HttpServletResponse response){
    ModelAndView mv = new ModelAndView("redirect:/index");
    session.removeAttribute(WEB_SESSION_USER);
    session.invalidate();
    return mv;
  }
  
  /**
   * pad端 接口Rest处理 接收pad 接口请求
   */
  @RequestMapping("/getMsg")
  public HashMap<String, Object> getMsg() {
    HashMap<String, Object> res = new HashMap<String, Object>();
    try {
      HashMap<String, Object> person = new HashMap<String, Object>();
      person.put("username", "taotao");
      person.put("address", "湖北");
      person.put("sex", "男");
      res.put("item", person);// 数据列表
      res.put("status", 1);// 成功
      res.put("msg", "hello");// 绑定变量
    } catch (Exception e) {
      e.printStackTrace();
    }
    return res;
  }
}
