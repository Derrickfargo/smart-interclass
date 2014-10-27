package com.incito.interclass.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.incito.interclass.business.ClassService;
import com.incito.interclass.business.SchoolService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Classes;
import com.incito.interclass.entity.School;
import com.incito.parent.pagehelper.PageHelper;
import com.incito.parent.pagehelper.PageInfo;

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
	public ModelAndView index(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
		ModelAndView res = new ModelAndView("class/classList");
		PageHelper.startPage(pageNum, PAGE_SIZE);
		List<Classes> classList = classService.getClassListByCondition();
		PageInfo<Classes> page = new PageInfo<Classes>(classList);
		res.addObject("page", page);
		res.addObject("pageNum", pageNum);
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
	
	/**
	 * 修改
	 */
	@RequestMapping("/edit")
	public ModelAndView edit(Integer classId) {
		ModelAndView res = new ModelAndView("class/classEdit");
		Classes classes = classService.getClassById(classId);
		List<School> schools = schoolService.getSchoolList();
		res.addObject("classes", classes);
		res.addObject("schools", schools);
		return res;
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public ModelAndView update(Classes classes) {
		boolean result = classService.update(classes);
		if (result) {
			return new ModelAndView("redirect:list");
		}
		return new ModelAndView("class/classEdit");
	}
}
