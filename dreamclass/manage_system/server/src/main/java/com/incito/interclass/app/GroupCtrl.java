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
	public String getGroupByIMEI(String imei,int teacherId,int courseId,int classId){
		Group group = groupService.getGroupByIMEI(imei, teacherId, courseId, classId);
		return renderJSONString(SUCCESS, group);
	}
	
	
	/**
	 * 根据教师id、课程id、班级id获取分组列表
	 * @param teacherId 教师id
	 * @param courseId 课程id
	 * @param classId 班级id
	 * @return
	 */
	@RequestMapping(value = "/list", produces = { "application/json;charset=UTF-8" })
	public String list(int teacherId,int courseId,int classId) {
		List<Group> groups = groupService.getGroupList(teacherId, courseId, classId);
		return renderJSONString(SUCCESS, groups);
	}
	
	@RequestMapping(value = "/update", produces = { "application/json;charset=UTF-8" })
	public String update(int id, String name, String logo) {
		Group group = new Group();
		group.setId(id);
		group.setName(name);
		group.setLogo(logo);
		try {
			groupService.updateGroup(group);
			group = groupService.getGroupById(id);
		} catch (Exception e) {
			return renderJSONString(1);//更新失败
		}
		return renderJSONString(SUCCESS, group);
	}
}
