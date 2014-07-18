package com.incito.interclass.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.incito.interclass.business.UserService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Student;
import com.incito.interclass.entity.User;

@RestController
@RequestMapping("/student")
public class StudentCtrl extends BaseCtrl {

	@Autowired
	private UserService userService;
	
	
	
	/**
	 * 保存学生
	 * @return
	 */
	@RequestMapping(value = "/save")
	public ModelAndView save(Student student) {
		student.setActive(true);
		//用户角色为学生
		student.setRole(User.ROLE_STUDENT);
		userService.saveStudent(student);
		return new ModelAndView("redirect:list");
	}
	
}
