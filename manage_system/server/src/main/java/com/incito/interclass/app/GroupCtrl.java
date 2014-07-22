package com.incito.interclass.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incito.base.exception.AppException;
import com.incito.interclass.business.GroupService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Group;

@RestController
@RequestMapping("/api/group")
public class GroupCtrl extends BaseCtrl {

	private static final int SAVE_GROUP_ERROR = 1;
	private static final int ADD_STUDENT_ERROR = 2;
	
	@Autowired
	private GroupService groupService;
	

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
}
