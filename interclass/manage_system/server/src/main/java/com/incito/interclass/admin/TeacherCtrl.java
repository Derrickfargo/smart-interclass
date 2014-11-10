package com.incito.interclass.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.incito.base.exception.AppException;
import com.incito.base.util.Md5Utils;
import com.incito.interclass.business.CourseService;
import com.incito.interclass.business.SchoolService;
import com.incito.interclass.business.UserService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Course;
import com.incito.interclass.entity.School;
import com.incito.interclass.entity.Teacher;
import com.incito.interclass.entity.User;
import com.incito.parent.pagehelper.PageHelper;
import com.incito.parent.pagehelper.PageInfo;

@RestController
@RequestMapping("/teacher")
public class TeacherCtrl extends BaseCtrl {

	@Autowired
	private UserService userService;
	
	@Autowired
	private SchoolService schoolService;
	
	@Autowired
	private CourseService courseService;
	/**
	 * 教师列表
	 */
	@RequestMapping("/list")
	public ModelAndView index(String name, String schoolName,
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
		ModelAndView res = new ModelAndView("teacher/teacherList");
		PageHelper.startPage(pageNum, PAGE_SIZE);
		List<Teacher> teachers = userService.getTeacherListByCondition(name,schoolName);
		PageInfo<Teacher> page = new PageInfo<Teacher>(teachers);
		res.addObject("page", page);
		res.addObject("name", name);
		res.addObject("schoolName", schoolName);
		return res;
	}
	
	/**
	 * 添加教师
	 */
	@RequestMapping("/add")
	public ModelAndView add() {
		ModelAndView mav = new ModelAndView("teacher/teacherAdd");
		List<School> schools = schoolService.getSchoolList();
		List<Course> courses = courseService.getCourseList();
		mav.addObject("schools", schools);
		mav.addObject("courses", courses);
		return mav;
	}
	
	/**
	 * 保存
	 * @return
	 */
	@RequestMapping(value = "/save")
	public ModelAndView save(Teacher teacher,Model model) {
		teacher.setActive(true);
		//身份证号码最后6位作为密码
		String password = teacher.getIdcard().substring(12);
		teacher.setPassword(Md5Utils.md5(password));
		//用户角色为老师
		teacher.setRole(User.ROLE_TEACHER);
		try {
			userService.saveTeacher(teacher);
		} catch (AppException e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:list");
	}
//	/**
//	 * 删除学生
//	 * @param teacherId
//	 * @return
//	 */
//	@RequestMapping(value = "/delete")
//	public ModelAndView delete(int teacherId) {
//		userService.deleteTeacher(teacherId);
//		return new ModelAndView("redirect:list");
//	}
	
	/**
	 * 新增教师时重复校验 
	 * @return
	 */
	
	@RequestMapping(value="/check")
	public boolean check(String uname,String idcard,@RequestParam(value = "id", defaultValue = "-1") int id){
		Teacher teacher = new Teacher();
		if(id!=-1){
			teacher =userService.getTeacherById(id);
		}
		if(uname!=null){
			if(uname.equals(teacher.getUname())){
				return true;
			}
			int flag=userService.getTeacherByUname(uname);
			if(flag!=0)
				return false;
		}
		if(idcard!=null){
			int flag=userService.getTeacherByIdCard(idcard);
			if(idcard.equals(teacher.getIdcard())){
				return true;
			}
			if(flag!=0)
				return false;
		}
		return true;
	}
	/**
	 * 更新教师信息
	 * 
	 * @param teacher
	 * @return
	 */
	@RequestMapping(value="/update")
	public ModelAndView update(Teacher teacher){
		try {
			userService.updateTeacherById(teacher);
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ModelAndView("redirect:list");
	}
	
	
	/**
	 * 
	 * 修改教师信息
	 * @param teacherId
	 * @return
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit(int teacherId){
		ModelAndView res= new ModelAndView("/teacher/teacherEdit");
		Teacher teacher=userService.getTeacherById(teacherId);
		List<School> schools = schoolService.getSchoolList();
		List<Course> courses = courseService.getCourseList();
		res.addObject("schools", schools);
		res.addObject("courses", courses);
		res.addObject("teacher",teacher);
		return res;
	}
}
