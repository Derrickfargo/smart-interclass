package com.incito.interclass.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.incito.interclass.business.UserService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Student;
import com.incito.interclass.entity.Teacher;

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
		if (teacher != null && teacher.getId() != 0) {
			List<Student> studentList = userService.getStudentListByMac(mac);
			return JSON.toJSONString(studentList);
		}
		return null;
	}

	/**
	 * 下载班级学生名单
	 */
	@RequestMapping(value = "/student_list", produces = { "application/json;charset=UTF-8" })
	public String getStudentList(String mac) {
		List<Student> studentList = userService.getStudentListByMac(mac);
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
