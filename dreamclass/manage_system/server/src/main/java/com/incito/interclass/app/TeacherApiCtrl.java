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
import com.incito.interclass.business.RoomService;
import com.incito.interclass.business.TableService;
import com.incito.interclass.business.UserService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Classes;
import com.incito.interclass.entity.Course;
import com.incito.interclass.entity.Device;
import com.incito.interclass.entity.Group;
import com.incito.interclass.entity.Room;
import com.incito.interclass.entity.Table;
import com.incito.interclass.entity.Teacher;

@RestController
@RequestMapping("/api/teacher")
public class TeacherApiCtrl extends BaseCtrl {

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
	private CourseService courseService;
	
	@Autowired
	private ClassService classService;
	
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private TableService tableService;
	
	@Autowired
	private DeviceService deviceService;
	
	/**
	 * 教师登陆
	 * @param uname
	 * @param password
	 * @param mac
	 * @return
	 */
	@RequestMapping(value = "/login", produces = { "application/json;charset=UTF-8" })
	public String login(String uname, String password, String mac) {
		Teacher teacher = new Teacher();
		teacher.setUname(uname);
		teacher.setPassword(password); 
		teacher = userService.loginForTeacher(teacher);
		if (teacher == null || teacher.getId() == 0) {
			return renderJSONString(USERNAME_OR_PASSWORD_ERROR);
		}
		//获取上课教室
		Room room = roomService.getRoomByMac(mac);
		//获取当前老师的课程列表
		List<Course> courses = courseService.getCourseList();
		
		TeacherLoginResultData data = new TeacherLoginResultData();
		data.setTeacher(teacher);
		data.setRoom(room);
		data.setCourses(courses);
		ApiResult result = new ApiResult();
		result.setCode(ApiResult.SUCCESS);
		result.setData(data);
		return  JSON.toJSONString(result);
	}
	
	/**
	 * 获得课堂分组
	 * @param teacherId
	 * @param courseId
	 * @param classId
	 * @param className
	 * @return
	 */
	@RequestMapping(value = "/group", produces = { "application/json;charset=UTF-8" })
	public String group(int schoolId, int roomId,int teacherId, int courseId, 
			int year, int classNumber) {
		Classes classes = classService.getClassByNumber(teacherId, year, classNumber);
		if (classes == null || classes.getId() == 0) {//不存在当前班级，添加
			classes = new Classes();
			classes.setYear(year);
			classes.setNumber(classNumber);
			classes.setTeacherId(teacherId);
			classes.setSchoolId(schoolId);
			classService.saveClass(classes);
		}
		//获得当前课堂的分组列表
		List<Group> groups = groupService.getGroupList(teacherId, courseId, classes.getId());
		//获取当前教室的课桌列表
		List<Table> tables = tableService.getTableListByRoomId(roomId);
		//获得当前教室的设备列表
		List<Device> devices = deviceService.getDeviceListByRoomId(roomId);
		//选择的课程
		Course course = courseService.getCourseById(courseId);
		
		TeacherGroupResultData data = new TeacherGroupResultData();
		data.setGroups(groups);
		data.setTables(tables);
		data.setDevices(devices);
		data.setCourse(course);
		data.setClasses(classes);
		ApiResult result = new ApiResult();
		result.setCode(ApiResult.SUCCESS);
		result.setData(data);
		return  JSON.toJSONString(result);
	}
}
