package com.incito.interclass.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.incito.interclass.business.SchoolService;
import com.incito.interclass.business.UserService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Teacher;
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
	
}
