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
import com.incito.interclass.business.SchoolService;
import com.incito.interclass.business.UserService;
import com.incito.interclass.common.BaseCtrl;
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
		mav.addObject("schools", schools);
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
	
	@RequestMapping(value = "/delete")
	public ModelAndView delete(int teacherId) {
		userService.deleteTeacher(teacherId);
		return new ModelAndView("redirect:list");
	}
}