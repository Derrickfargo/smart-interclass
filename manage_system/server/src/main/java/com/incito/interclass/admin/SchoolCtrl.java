package com.incito.interclass.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.incito.interclass.business.SchoolService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Log;
import com.incito.interclass.entity.School;

@RestController
@RequestMapping("/school")
public class SchoolCtrl extends BaseCtrl {

	@Autowired
	private SchoolService schoolService;
	
	/**
	 * 学校列表
	 */
	@RequestMapping("/list")
	public ModelAndView index(School school,
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
		ModelAndView res = new ModelAndView("school/schoolList");
		PageHelper.startPage(pageNum, PAGE_SIZE);
		List<School> schools = schoolService.getSchoolList();
		PageInfo<School> page = new PageInfo<School>(schools);
		res.addObject("page", page);
		return res;
	}
	
	/**
	 * 添加學校
	 */
	@RequestMapping("/add")
	public ModelAndView add() {
		return new ModelAndView("school/schoolAdd");
	}
	
	/**
	 * 保存
	 * @return
	 */
	@RequestMapping(value = "/save")
	public ModelAndView save(School school) {
		schoolService.saveSchool(school);
		return new ModelAndView("redirect:list");
	}
	
	@RequestMapping(value = "/delete")
	public ModelAndView delete(int schoolId) {
		schoolService.deleteSchool(schoolId);
		return new ModelAndView("redirect:list");
	}
}
