package com.incito.interclass.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incito.base.exception.AppException;
import com.incito.interclass.business.UserService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Student;
import com.incito.interclass.entity.User;

@RestController
@RequestMapping("/api/student")
public class StudentCtrl extends BaseCtrl {

	private static final int REGISTER_ERROR = 1;
	private static final int LOGIN_ERROR = 1;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 学生注册
	 * @param student
	 * @return
	 */
	@RequestMapping(value = "/register", produces = { "application/json;charset=UTF-8" })
	public String register(Student student) {
		student.setActive(true);
		student.setRole(User.ROLE_STUDENT);
		try {
			if (!userService.saveStudent(student)) {
				return renderJSONString(REGISTER_ERROR);
			}
		} catch (AppException e) {
			return renderJSONString(REGISTER_ERROR);
		}
		return renderJSONString(SUCCESS, student);
	}

	/**
	 * 学生登陆
	 * @param student
	 * @return
	 */
	@RequestMapping(value = "/login", produces = { "application/json;charset=UTF-8" })
	public String login(Student student){
		Student result = userService.loginForStudent(student);
		if (result == null || result.getId() == 0) {
			return renderJSONString(LOGIN_ERROR);//
		}
		return renderJSONString(SUCCESS, result);
	}
	
}
