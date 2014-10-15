package com.incito.interclass.admin;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.incito.base.exception.AppException;
import com.incito.interclass.business.ClassService;
import com.incito.interclass.business.DeviceService;
import com.incito.interclass.business.SchoolService;
import com.incito.interclass.business.UserService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.constant.Constants;
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
	public ModelAndView save(Integer schoolId, Integer year, Integer classNumber, String name, 
			String number, Integer sex, String guardian, String phone, String address, String imei) {
		//班年级转换为入学学年
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH) + 1;
		if(month < 9){
			calendar.add(Calendar.YEAR, year * -1);
		} else {
			calendar.add(Calendar.YEAR, (year-1) * -1);
		}
		int newYear = calendar.get(Calendar.YEAR);
		Classes classes = classService.getClassByNumber(schoolId, newYear, classNumber);
		if (classes == null || classes.getId() == 0) {
			classes = new Classes();
			classes.setNumber(classNumber);
			classes.setSchoolId(schoolId);
			classes.setYear(newYear);
			classService.saveClass(classes);
		}
		Student student = new Student();
		student.setClassId(classes.getId());
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
		deviceService.saveDevice(device);
		student.setDeviceId(device.getId());
		userService.saveStudent(student);
		return new ModelAndView("redirect:list");
	}
	
	@RequestMapping(value = "/delete")
	public ModelAndView delete(int studentId) {
		userService.deleteStudent(studentId);
		return new ModelAndView("redirect:list");
	}
	
	@RequestMapping(value = "/import")
	public ModelAndView importStudent() {
		return new ModelAndView("student/import");
	}
	
	/**
	 * 批量导入
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/batchSave")
	public ModelAndView batchSave(MultipartFile file) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat time = new SimpleDateFormat("HH-mm-ss");
		String filename = file.getOriginalFilename();
		File dir = new File(Constants.STUDENT_DIR + File.separator
				+ sdf.format(date) + File.separator + time.format(date));
		dir.mkdirs();
		File newFile = new File(dir, filename);
		try {
			file.transferTo(newFile);
		} catch (IOException e) {
			ModelAndView mav = new ModelAndView("student/import");
			mav.addObject("message", "文件获取失败，请重试！");
			return mav;
		}
		Map<String, Object> result = userService.saveStudent(newFile);
		ModelAndView mav = new ModelAndView("redirect:list");
		Object code = result.get("code");
		if (code != null && Integer.parseInt(code.toString()) != 0){
			mav.addObject("code", Integer.parseInt(code.toString()));
		}
		mav.addObject("exists", result.get("exists"));
		mav.addObject("unbind", result.get("unbind"));
		return mav;
	}
}
