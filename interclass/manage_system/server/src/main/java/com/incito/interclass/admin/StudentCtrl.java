package com.incito.interclass.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
import com.incito.parent.pagehelper.PageHelper;
import com.incito.parent.pagehelper.PageInfo;

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
	public ModelAndView index(String name, String schoolName,
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
		ModelAndView res = new ModelAndView("student/studentList");
		PageHelper.startPage(pageNum, PAGE_SIZE);
		List<Student> students = userService.getStudentListByCondition(name, schoolName);
		PageInfo<Student> page = new PageInfo<Student>(students);
		res.addObject("page", page);
		res.addObject("pageNum", pageNum);
		res.addObject("name", name);
		res.addObject("schoolName", schoolName);
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
			List<Classes> classes = classService.getClassList(school.getId());
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
	public ModelAndView save(Integer classId, String name, Integer sex,
			String guardian, String phone, String address, String imei) {
		Student student = new Student();
		student.setClassId(classId);
		student.setUname(name);
		student.setName(name);
		student.setSex(sex);
		student.setGuardian(guardian);
		student.setPhone(phone);
		student.setAddress(address);
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
