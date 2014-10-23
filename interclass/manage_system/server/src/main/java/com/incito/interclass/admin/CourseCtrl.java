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
	
	@RequestMapping("/list")
	public ModelAndView index(Course course,
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
		ModelAndView res = new ModelAndView("course/courseList");
		PageHelper.startPage(pageNum, PAGE_SIZE);
		List<Course> courses = courseService.getCourseList();
		PageInfo<Course> page = new PageInfo<Course>(courses);
		res.addObject("page", page);
		return res;
	}
	
	@RequestMapping("/add")
	public ModelAndView add() {
		return new ModelAndView("course/courseAdd");
	}
	
	@RequestMapping(value = "/save")
	public ModelAndView save(Course course) {
		courseService.saveCourse(course);
		return new ModelAndView("redirect:list");
	}
	
	@RequestMapping(value = "/delete")
	public ModelAndView delete(int courseId) {
		courseService.deleteCourse(courseId);
		return new ModelAndView("redirect:list");
	}
}
