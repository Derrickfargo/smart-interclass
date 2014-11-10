package com.incito.interclass.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.incito.interclass.business.SchoolService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.School;
import com.incito.parent.pagehelper.PageHelper;
import com.incito.parent.pagehelper.PageInfo;

@RestController
@RequestMapping("/school")
public class SchoolCtrl extends BaseCtrl {

	@Autowired
	private SchoolService schoolService;
	
	/**
	 * 学校列表
	 */
	@RequestMapping("/list")
	public ModelAndView index(String name, @RequestParam(value = "schoolType", defaultValue = "-1") Integer schoolType,
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
		ModelAndView res = new ModelAndView("school/schoolList");
		PageHelper.startPage(pageNum, PAGE_SIZE);
		List<School> schools = schoolService.getSchoolListByCondition(name,schoolType);
		PageInfo<School> page = new PageInfo<School>(schools);
		res.addObject("page", page);
		res.addObject("name", name);
		res.addObject("schoolType", schoolType);
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
//	/**
//	 * 
//	 * 删除学校
//	 *
//	 */
//	@RequestMapping(value = "/delete")
//	public ModelAndView delete(int schoolId) {
//		schoolService.deleteSchool(schoolId);
//		return new ModelAndView("redirect:list");
//	}
	
	/**
	 * 按名字查询学校是否存在
	 * 
	 * 
	 * @return
	 */
	@RequestMapping(value="/check",produces={"application/json;charset=UTF-8"})
	public boolean check(String name,@RequestParam(value="id",defaultValue="-1")int id){
		School school=new School();
		if(id!=-1){
			school=schoolService.getSchoolById(id);
		}
		if(name!=null){
			while(name.equals(school.getName())){
				return true;
			}
			int flag=schoolService.searchSchoolByName(name);
			if(flag!=0){
				return false;
			}
		}
		return true;
	}
	/**
	 * 学校编辑，按ID查找学校
	 * 
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit(int schoolId){
		ModelAndView res= new ModelAndView("school/schoolEdit");
		School school=schoolService.getSchoolById(schoolId);
		res.addObject(school);
		return res;
	}
	/**
	 * 学校更新，按ID修改学校
	 * 
	 */
	@RequestMapping(value="/update")
	public ModelAndView update(School school){
		schoolService.editSchool(school);
		return new ModelAndView("redirect:list");
	}
	/**
	 * 学校模糊查询
	 * @param name
	 * @return
	 */
	@RequestMapping(value="/search",produces = { "application/json;charset=UTF-8" })
	public String search(String name){
		List<School> schools=schoolService.getSchoolListByCondition(name, -1);
		Map<String,Object> school=new HashMap<String, Object>();
		school.put("name",schools);
		return JSON.toJSONString(schools);
	}
}
