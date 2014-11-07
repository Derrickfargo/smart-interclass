package com.incito.interclass.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incito.interclass.business.GroupService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Group;

@RestController
@RequestMapping("/api/group")
public class GroupCtrl extends BaseCtrl {

	@Autowired
	private GroupService groupService;
	
	
	/**
	 * 保存分组
	 * @param group
	 * @return
	 */
	@RequestMapping(value = "/save", produces = { "application/json;charset=UTF-8" })
	public String saveGroup(Group group) {
		return renderJSONString(SUCCESS, groupService.save(group));
	}
	
	
	

	/**
	 * 根据IMEI获得分组
	 * @param imei
	 * @return
	 */
	@RequestMapping(value = "/get", produces = { "application/json;charset=UTF-8" })
	public String getGroupByIMEI(String imei,int teacherId,int classId,String courseName){
		Group group = groupService.getGroupByIMEI(imei, teacherId, classId);
		return renderJSONString(SUCCESS, group);
	}
	
	
	/**
	 * 根据教师id、班级id获取分组列表
	 * @param teacherId 教师id
	 * @param classId 班级id
	 * @return
	 */
	@RequestMapping(value = "/list", produces = { "application/json;charset=UTF-8" })
	public String list(int teacherId,int classId) {
		List<Group> groups = groupService.getGroupList(teacherId, classId);
		return renderJSONString(SUCCESS, groups);
	}
}
