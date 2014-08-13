package com.incito.interclass.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.incito.interclass.business.UserService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Admin;
import com.incito.interclass.entity.User;

/**
 * 后台端 请求处理
 * 
 * @author 刘世平
 * 
 */
@RestController
@RequestMapping("/")
public class IndexCtrl extends BaseCtrl {

	@Autowired
	private UserService userService;

	/**
	 * 后台首页-登录
	 */
	@RequestMapping("")
	public ModelAndView loginindex() {
		return new ModelAndView("login");// 对应 login.jsp
	}

	/**
	 * 后台首页-登录
	 */
	@RequestMapping("/login")
	public ModelAndView login(User user, Model model) {
		ModelAndView res = new ModelAndView("redirect:index");// 登录成功 跳转到 index
		try {
			Admin admin = userService.loginForAdmin(user);
			if (admin == null) {
				res.setViewName("login");// 验证失败 跳转到 login.jsp
				return res;
			}
			res.addObject("user", admin);
			model.addAttribute("user", user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 后台首页展示
	 */
	@RequestMapping("/index")
	public ModelAndView index() {
		// 默认进入School页面
		return new ModelAndView("redirect:school/list");
	}

}
