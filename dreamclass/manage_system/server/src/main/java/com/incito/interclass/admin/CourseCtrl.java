package com.incito.interclass.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.incito.interclass.business.CourseService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Course;
import com.incito.parent.pagehelper.PageHelper;
import com.incito.parent.pagehelper.PageInfo;

@RestController
@RequestMapping("/course")
public class CourseCtrl extends BaseCtrl {

	@Autowired
	private CourseService courseService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public ModelAndView index(String name,
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
		ModelAndView res = new ModelAndView("course/courseList");
		PageHelper.startPage(pageNum, PAGE_SIZE);
		List<Course> courses = courseService.getCourseListByCondition(name);
		PageInfo<Course> page = new PageInfo<Course>(courses);
		res.addObject("page", page);
		res.addObject("name", name);
		return res;
	}
	
	/**
	 * 添加
	 */
	@RequestMapping("/add")
	public ModelAndView add() {
		return new ModelAndView("course/courseAdd");
	}
	
}
