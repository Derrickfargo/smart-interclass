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
	 * 根据教师id、课程id、班级id获取分组列表
	 * @param teacherId
	 * @param courseId
	 * @param classId
	 * @return
	 */
	@RequestMapping(value = "/list", produces = { "application/json;charset=UTF-8" })
	public String list(int teacherId,int courseId,int classId) {
		List<Group> groups = groupService.getGroupList(teacherId, courseId, classId);
		return renderJSONString(SUCCESS, groups);
	}

}
