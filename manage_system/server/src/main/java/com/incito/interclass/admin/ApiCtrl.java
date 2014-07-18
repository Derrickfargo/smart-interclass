package com.incito.interclass.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.incito.interclass.business.UserService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Student;
import com.incito.interclass.entity.Teacher;
import com.incito.interclass.entity.User;

/**
 * 后台端 请求处理
 * 
 * @author 刘世平
 * 
 */
@RestController
@RequestMapping("/api")
public class ApiCtrl extends BaseCtrl {

	@Autowired
	private UserService userService;

	/**
	 * 教师登录
	 */
	@RequestMapping(value = "/teacher_login", produces = { "application/json;charset=UTF-8" })
	public String login(String mac, String uname, String password) {
		Teacher teacher = userService.loginForTeacher(uname, password);
		return JSON.toJSONString(teacher);
	}

	/**
	 * 学生注册
	 * @return
	 */
	@RequestMapping(value = "/student/save", produces = { "application/json;charset=UTF-8" })
	public ModelAndView save(Student student) {
		student.setActive(true);
		//用户角色为学生
		student.setRole(User.ROLE_STUDENT);
		userService.saveStudent(student);
		return new ModelAndView("redirect:list");
	}
	
	/**
	 * 获取某组学生名单
	 */
	@RequestMapping(value = "/student_list", produces = { "application/json;charset=UTF-8" })
	public String getStudentList(int groupId) {
		List<Student> studentList = userService.getStudentByGroupId(groupId);
		return JSON.toJSONString(studentList);
	}

	/**
	 * 学生登录
	 */
	@RequestMapping(value = "/student_login", produces = { "application/json;charset=UTF-8" })
	public String login(String imei) {
		Student student = userService.loginForStudent(imei);
		return JSON.toJSONString(student);
	}

}
