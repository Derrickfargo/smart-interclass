package com.incito.interclass.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.incito.interclass.business.CourseService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Course;

@RestController
@RequestMapping("/course")
public class CourseCtrl extends BaseCtrl {

	@Autowired
	private CourseService courseService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public ModelAndView index(Course course,Integer page) {
		ModelAndView res = new ModelAndView("course/courseList");
		if (page == null) {
			page = 0;
		}
		List<Course> courses = courseService.getCourseList(course, page * maxResults, maxResults);
		res.addObject("courses", courses);
		return res;
	}
	
	/**
	 * 添加
	 */
	@RequestMapping("/add")
	public ModelAndView add() {
		return new ModelAndView("course/courseAdd");
	}
	
	/**
	 * 保存
	 * @return
	 */
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
