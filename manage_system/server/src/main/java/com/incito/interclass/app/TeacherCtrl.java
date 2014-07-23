package com.incito.interclass.app;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.incito.base.util.Md5Utils;
import com.incito.interclass.app.result.ApiResult;
import com.incito.interclass.app.result.TeacherLoginResultData;
import com.incito.interclass.business.ClassService;
import com.incito.interclass.business.CourseService;
import com.incito.interclass.business.UserService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Classes;
import com.incito.interclass.entity.Course;
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
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private ClassService classService;
	
	@RequestMapping(value = "/login", produces = { "application/json;charset=UTF-8" })
	public String login(Teacher teacher){
		String password = teacher.getPassword();
		teacher.setPassword(Md5Utils.md5(password)); 
		teacher = userService.loginForTeacher(teacher);
		if (teacher == null || teacher.getId() == 0) {
			return renderJSONString(USERNAME_OR_PASSWORD_ERROR);
		}
		//获取当前老师的课程列表
		List<Course> courses = courseService.getCourseList();
		//获取当前老师的教室列表
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		List<Classes> classes = classService.getClassList(teacher.getId(), year);
		
		TeacherLoginResultData data = new TeacherLoginResultData();
		data.setTeacher(teacher);
		data.setClasses(classes);
		data.setCourses(courses);
		ApiResult result = new ApiResult();
		result.setCode(ApiResult.SUCCESS);
		result.setData(data);
		return  JSON.toJSONString(result);
	}
	
}
