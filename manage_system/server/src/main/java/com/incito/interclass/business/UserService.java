package com.incito.interclass.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	public Teacher loginForTeacher(String uname,String password){
		Teacher teacher = new Teacher();
		teacher.setUname(uname);
		teacher.setPassword(password); 
		return userMapper.loginForTeacher(teacher);
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
	
	@Transactional(rollbackFor = RuntimeException.class)
	public boolean saveTeacher(Teacher teacher) {
		userMapper.saveUser(teacher);
		if(teacher.getId() <= 0){
			throw new RuntimeException();
		}
		userMapper.saveTeacher(teacher);
		return true;
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public boolean saveStudent(Student student) {
		int id = (Integer) userMapper.saveUser(student);
		if(id <= 0){
			throw new RuntimeException();
		}
		student.setId(id);
		userMapper.saveStudent(student);
		return true;
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
