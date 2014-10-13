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
	 * 学生创建小组
	 * @param name
	 * @param logo
	 * @param teacherId
	 * @param classId
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value = "/creat", produces = { "application/json;charset=UTF-8" })
	public String creatGroup(Group group){
		 Integer result = groupService.creatGroup(group);
		 if(result==1){
			 return renderJSONString(SUCCESS, group);
		 }else{
			 return renderJSONString(1);
		 }
	}
	/**
	 * 删除小组
	 * @param name
	 * @param logo
	 * @param teacherId
	 * @param classId
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value = "/delete", produces = { "application/json;charset=UTF-8" })
	public String deleteGroup(String groupId){
		Integer result =groupService.deleteGroupById(groupId);
		if(result==1){
			 return renderJSONString(SUCCESS);
		 }else{
			 return renderJSONString(1);
		 }
	}
	
	/**
	 * 学生加入小组
	 * @param groupId
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value = "/join", produces = { "application/json;charset=UTF-8" })
	public String joinGroup(String groupId,String studentId){
		Group group = groupService.joinGroup(groupId,studentId);
		return renderJSONString(SUCCESS, group);
	}
	/**
	 * 根据IMEI获得分组
	 * @param imei
	 * @return
	 */
	@RequestMapping(value = "/get", produces = { "application/json;charset=UTF-8" })
	public String getGroupByIMEI(String imei,int teacherId,int classId){
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
