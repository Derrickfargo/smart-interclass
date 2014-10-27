package com.incito.interclass.app;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.incito.interclass.app.result.ApiResult;
import com.incito.interclass.app.result.TeacherGroupResultData;
import com.incito.interclass.app.result.TeacherLoginResultData;
import com.incito.interclass.business.ClassService;
import com.incito.interclass.business.CourseService;
import com.incito.interclass.business.DeviceService;
import com.incito.interclass.business.GroupService;
import com.incito.interclass.business.UserService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Classes;
import com.incito.interclass.entity.Course;
import com.incito.interclass.entity.Group;
import com.incito.interclass.entity.Student;
import com.incito.interclass.entity.Teacher;

@RestController
@RequestMapping("/api/teacher")
public class TeacherCtrl extends BaseCtrl {

	/**
	 * 登陆失败，用户名或者密码错误
	 */
	private static final int USERNAME_OR_PASSWORD_ERROR = 1;
	/**
	 * 保存班级失败
	 */
	private static final int SAVE_CLASS_ERROR = 2;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ClassService classService;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private CourseService courseService;
	
	/**
	 * 教师登陆
	 * @param uname
	 * @param password
	 * @param mac
	 * @return
	 */
	@RequestMapping(value = "/login", produces = { "application/json;charset=UTF-8" })
	public String login(String uname, String password) {
		Teacher teacher = new Teacher();
		teacher.setUname(uname);
		teacher.setPassword(password); 
		teacher = userService.loginForTeacher(teacher);
		if (teacher == null || teacher.getId() == 0) {
			return renderJSONString(USERNAME_OR_PASSWORD_ERROR);
		}
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		List<Classes> classes = classService.getClassList(year);
		Course course = courseService.getCourseById(teacher.getCourseId());
		TeacherLoginResultData data = new TeacherLoginResultData();
		data.setTeacher(teacher);
		data.setClasses(classes);
		data.setCourse(course);
		ApiResult result = new ApiResult();
		result.setCode(ApiResult.SUCCESS);
		result.setData(data);
		return  JSON.toJSONString(result);
	}
	
	/**
	 * 获得课堂分组
	 * @param teacherId
	 * @param classId 班级
	 * @param className
	 * @return
	 */
	@RequestMapping(value = "/group", produces = { "application/json;charset=UTF-8" })
	public String group(int schoolId, int teacherId, int courseId, int year,
			int classNumber) {
		Classes classes = classService.getClassByNumber(schoolId, year, classNumber);
		if (classes == null || classes.getId() == 0) {//不存在当前班级，添加
			classes = new Classes();
			classes.setYear(year);
			classes.setNumber(classNumber);
			classes.setSchoolId(schoolId);
			classService.saveClass(classes);
		}
		//获得当前课堂的分组列表
		List<Group> groups = groupService.getGroupList(teacherId,classes.getId());
		List<Student> students = userService.getStudentByClassId(classes.getId());
		Course course = courseService.getCourseById(courseId);
		TeacherGroupResultData data = new TeacherGroupResultData();
		data.setCourse(course);
		data.setGroups(groups);
		data.setClasses(classes);
		data.setStudents(students);
		ApiResult result = new ApiResult();
		result.setCode(ApiResult.SUCCESS);
		result.setData(data);
		return  JSON.toJSONString(result);
	}
}
