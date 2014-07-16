package com.incito.interclass.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.incito.interclass.business.ClassService;
import com.incito.interclass.business.SchoolService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Classes;
import com.incito.interclass.entity.School;

@RestController
@RequestMapping("/class")
public class ClassCtrl extends BaseCtrl {

	@Autowired
	private ClassService classService;
	
	@Autowired
	private SchoolService schoolService;
	
	/**
	 * 班级
	 */
	@RequestMapping("/list")
	public ModelAndView index(Classes classes,Integer page) {
		ModelAndView res = new ModelAndView("class/classList");
		if (page == null) {
			page = 0;
		}
		List<Classes> classList = classService.getClassList(classes, page * maxResults, maxResults);
		res.addObject("classList", classList);
		return res;
	}
	
	/**
	 * 添加班级
	 */
	@RequestMapping("/add")
	public ModelAndView add() {
		ModelAndView mav = new ModelAndView("class/classAdd");
		List<School> schools = schoolService.getSchoolList();
		mav.addObject("schools", schools);
		return mav;
	}
	
	/**
	 * 保存
	 * @return
	 */
	@RequestMapping(value = "/save")
	public ModelAndView save(Classes classes) {
		classService.saveClass(classes);
		return new ModelAndView("redirect:/class/list");
	}
	
	@RequestMapping(value = "/delete")
	public ModelAndView delete(int classId) {
		classService.deleteClass(classId);
		return new ModelAndView("redirect:list");
	}
}
