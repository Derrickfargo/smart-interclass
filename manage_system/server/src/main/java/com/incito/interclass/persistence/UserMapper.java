package com.incito.interclass.persistence;

import java.util.List;

import com.incito.interclass.entity.Admin;
import com.incito.interclass.entity.Student;
import com.incito.interclass.entity.Teacher;
import com.incito.interclass.entity.User;

public interface UserMapper {
	Admin loginForAdmin(Admin admin);

	Teacher loginForTeacher(Teacher teacher);

	List<Student> getStudentByGroupId(int groupId);

	List<Teacher> getTeacherList();

	List<Student> getStudentList();

	Integer saveUser(User user);
	
	Integer saveTeacher(Teacher teacher);

	Integer saveStudent(Student student);

	void deleteUser(int id);
	
	void deleteTeacher(int teacherId);

	void deleteStudent(int studentId);

}
