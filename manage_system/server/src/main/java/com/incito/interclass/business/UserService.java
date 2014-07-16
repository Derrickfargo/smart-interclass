package com.incito.interclass.business;

import java.util.List;

import org.springframework.stereotype.Service;

import com.incito.base.dao.BaseService;
import com.incito.base.util.Md5Utils;
import com.incito.interclass.entity.Admin;
import com.incito.interclass.entity.Student;
import com.incito.interclass.entity.Teacher;
import com.incito.interclass.entity.User;

@Service
public class UserService extends BaseService {

	/**
	 * 管理员登陆
	 * 
	 * @param user
	 * @return
	 */
	public Admin loginForAdmin(User user) {
		Admin admin = new Admin();
		admin.setUname(user.getUname());
		admin.setPassword(Md5Utils.md5(user.getPassword()));
		return (Admin) findForObject("loginForAdmin", admin);
	}

	public Teacher loginForTeacher(String uname,String password){
		Teacher teacher = new Teacher();
		teacher.setUname(uname);
		teacher.setPassword(password); 
		return (Teacher) findForObject("loginForTeacher", teacher);
	}
	
	public List<Student> getStudentListByMac(String mac) {
		return findForList("getStudentListByMac", mac);
	}
	
	public Student loginForStudent(String deviceId){
		return (Student) findForObject("loginForStudent", deviceId);
	}
	
	public List<Teacher> getTeacherList(Object parameterObject, int skipResults,
			int maxResults) {
		return findForList("getTeacherList", parameterObject, skipResults,
				maxResults);
	}
	
	public List<Student> getStudentList(Object parameterObject, int skipResults,
			int maxResults) {
		return findForList("getStudentList", parameterObject, skipResults,
				maxResults);
	}
	
	public boolean saveTeacher(Teacher teacher) {
		int id = (Integer) addObject("saveUser", teacher);
		if (id != 0) {
			teacher.setId(id);
			addObject("saveTeacher", teacher);
			return true;
		}
		return false;
	}

	public boolean saveStudent(Student student) {
		int id = (Integer) addObject("saveUser", student);
		if (id != 0) {
			student.setId(id);
			addObject("saveStudent", student);
			return true;
		}
		return false;
	}

	public void deleteTeacher(int teacherId) {
		delObject("deleteUser", teacherId);
		delObject("deleteTeacher", teacherId);
	}
	
	public void deleteStudent(int studentId) {
		delObject("deleteUser", studentId);
		delObject("deleteStudent", studentId);
	}
}
