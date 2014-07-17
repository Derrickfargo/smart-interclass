package com.incito.interclass.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.incito.interclass.business.GroupService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Group;

@RestController
@RequestMapping("/group")
public class GroupCtrl extends BaseCtrl {

	@Autowired
	private GroupService groupService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public ModelAndView index(Group group,Integer page) {
		ModelAndView res = new ModelAndView("group/groupList");
		if (page == null) {
			page = 0;
		}
		List<Group> groups = groupService.getGroupList(group, page * maxResults, maxResults);
		res.addObject("groups", groups);
		return res;
	}
	
	/**
	 * 添加
	 */
	@RequestMapping("/add")
	public ModelAndView add() {
		return new ModelAndView("group/groupAdd");
	}
	
	/**
	 * 保存
	 * @return
	 */
	@RequestMapping(value = "/save")
	public ModelAndView save(Group group) {
		groupService.saveGroup(group);
		return new ModelAndView("redirect:list");
	}
	
	@RequestMapping(value = "/delete")
	public ModelAndView delete(int groupId) {
		groupService.deleteGroup(groupId);
		return new ModelAndView("redirect:list");
	}
}
