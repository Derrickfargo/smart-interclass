package com.incito.interclass.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incito.base.util.Md5Utils;
import com.incito.interclass.business.UserService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Teacher;

@RestController
@RequestMapping("/api/teacher")
public class TeacherCtrl extends BaseCtrl {

	/**
	 * 登陆失败，用户名或者密码错误
	 */
	private static final int USERNAME_OR_PASSWORD_ERROR = 1;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/login", produces = { "application/json;charset=UTF-8" })
	public String login(Teacher teacher){
		String password = teacher.getPassword();
		teacher.setPassword(Md5Utils.md5(password)); 
		Teacher result = userService.loginForTeacher(teacher);
		if (result == null || result.getId() == 0) {
			return renderJSONString(USERNAME_OR_PASSWORD_ERROR);
		}
		return renderJSONString(SUCCESS, result);
	}
	
}
