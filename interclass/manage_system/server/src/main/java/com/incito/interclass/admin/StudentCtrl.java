package com.incito.interclass.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.incito.base.exception.AppException;
import com.incito.interclass.business.ClassService;
import com.incito.interclass.business.DeviceService;
import com.incito.interclass.business.SchoolService;
import com.incito.interclass.business.UserService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Classes;
import com.incito.interclass.entity.Device;
import com.incito.interclass.entity.School;
import com.incito.interclass.entity.Student;
import com.incito.interclass.entity.User;

@RestController
@RequestMapping("/student")
public class StudentCtrl extends BaseCtrl {

	@Autowired
	private UserService userService;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private ClassService classService;
	@Autowired
	private DeviceService deviceService;
	
	/**
	 * 学生列表
	 */
	@RequestMapping("/list")
	public ModelAndView index(Student student,Integer page) {
		ModelAndView res = new ModelAndView("student/studentList");
		if (page == null) {
			page = 0;
		}
		List<Student> students = userService.getStudentList(student, page * maxResults, maxResults);
		res.addObject("students", students);
		
		return res;
	}
	
	/**
	 * 添加学生
	 */
	@RequestMapping("/add")
	public ModelAndView add() {
		ModelAndView mav = new ModelAndView("student/studentAdd");
		List<School> schools = schoolService.getSchoolList();
		if(schools != null && schools.get(0) != null){
			School school = schools.get(0);
			List<Classes> classes = classService.getClassBySchoolId(school.getId());
			mav.addObject("classes", classes);
		}
		mav.addObject("schools", schools);
		return mav;
	}
	
	/**
	 * 保存学生
	 * @return
	 */
	@RequestMapping(value = "/save")
	public ModelAndView save(String name, String imei) {
		Student student = new Student();
		student.setActive(true);
		//用户角色为学生
		student.setRole(User.ROLE_STUDENT);
		Device device = new Device();
		device.setImei(imei);
		int id = deviceService.saveDevice(device);
		student.setDeviceId(id);
		try {
			userService.saveStudent(student);
		} catch (AppException e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:list");
	}
	
	@RequestMapping(value = "/delete")
	public ModelAndView delete(int studentId) {
		userService.deleteStudent(studentId);
		return new ModelAndView("redirect:list");
	}
}
