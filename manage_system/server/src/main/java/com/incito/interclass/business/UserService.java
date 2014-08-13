package com.incito.interclass.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incito.base.exception.AppException;
import com.incito.base.util.Md5Utils;
import com.incito.interclass.entity.Admin;
import com.incito.interclass.entity.Student;
import com.incito.interclass.entity.Teacher;
import com.incito.interclass.entity.User;
import com.incito.interclass.persistence.UserMapper;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;
	
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
		return userMapper.loginForAdmin(admin);
	}

	public Teacher loginForTeacher(Teacher teacher){
		return userMapper.loginForTeacher(teacher);
	}
	
	public Student loginForStudent(Student student){
		return userMapper.loginForStudent(student);
	}
	
	public List<Student> getStudentByGroupId(int groupId) {
		return userMapper.getStudentByGroupId(groupId);
	}
	
	public List<Teacher> getTeacherList(Object parameterObject, int skipResults,
			int maxResults) {
		return userMapper.getTeacherList();
	}
	
	public List<Student> getStudentList(Object parameterObject, int skipResults,
			int maxResults) {
		return userMapper.getStudentList();
	}
	
	public Student getStudent(String name, String number) {
		return userMapper.getStudent(name, number);
	}
	
	@Transactional(rollbackFor = AppException.class)
	public boolean saveTeacher(Teacher teacher) throws AppException {
		userMapper.saveUser(teacher);
		if(teacher.getId() <= 0){
			throw AppException.database(0);
		}
		int result = userMapper.saveTeacher(teacher);
		return result == 1;
	}

	@Transactional(rollbackFor = AppException.class)
	public boolean saveStudent(Student student) throws AppException {
		userMapper.saveUser(student);
		if(student.getId() <= 0){
			throw AppException.database(0);
		}
		int result = userMapper.saveStudent(student);
		return result == 1;
	}

	public void deleteTeacher(int teacherId) {
		userMapper.deleteUser(teacherId);
		userMapper.deleteTeacher(teacherId);
	}
	
	public void deleteStudent(int studentId) {
		userMapper.deleteUser(studentId);
		userMapper.deleteStudent(studentId);
	}
}
