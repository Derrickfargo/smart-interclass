package com.incito.interclass.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incito.base.exception.AppException;
import com.incito.interclass.business.GroupService;
import com.incito.interclass.business.UserService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Group;
import com.incito.interclass.entity.Student;

@RestController
@RequestMapping("/api/student")
public class StudentCtrl extends BaseCtrl {

	private static final int REGISTER_ERROR = 3;
	private static final int LOGIN_ERROR = 1;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private GroupService groupService;
	

	/**
	 * 注册学生
	 * @param courseId 课程id
	 * @param classId 课程id
	 * @param teacherId 教师id
	 * @param tableId 课桌id
	 * @param studentId 学生id
	 * @return
	 */
	@RequestMapping(value = "/login", produces = { "application/json;charset=UTF-8" })
	public String addStudent(int courseId, int classId, int teacherId,
			int tableId, String name, String number, int sex, String imei) {
		// 查学生是否存在
		Student student = userService.getStudent(name, number);
		// 不存在保存学生
		if (student == null || student.getId() == 0) {
			student = new Student();
			student.setSex(sex);
			student.setUname(name);
			student.setName(name);
			student.setNumber(number);
			try {
				userService.saveStudent(student);
			} catch (AppException e) {
				return renderJSONString(REGISTER_ERROR);
			}
		}
		
		//根据学生id保存组
		try {
			Group group = groupService.addStudent(courseId, classId, teacherId, tableId, student.getId());
			if (group == null || group.getId() == 0) {
				return renderJSONString(REGISTER_ERROR);
			} else {
				group = groupService.getGroupById(group.getId());
				return renderJSONString(SUCCESS, group);
			}
		} catch (AppException e) {
			return renderJSONString(REGISTER_ERROR);
		}
	}
	
	@RequestMapping(value = "/test", produces = { "application/json;charset=UTF-8" })
	public String test(){
		Group group = groupService.getGroupById(2);
		return renderJSONString(SUCCESS, group);
	}
}
