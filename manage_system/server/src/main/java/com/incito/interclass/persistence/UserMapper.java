package com.incito.interclass.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.incito.interclass.entity.Admin;
import com.incito.interclass.entity.Student;
import com.incito.interclass.entity.Teacher;
import com.incito.interclass.entity.User;

public interface UserMapper {
	Admin loginForAdmin(Admin admin);

	Teacher loginForTeacher(Teacher teacher);
	
	Student loginForStudent(Student student);

	List<Student> getStudentByGroupId(int groupId);

	List<Teacher> getTeacherList();

	List<Student> getStudentList();
	
	Student getStudent(@Param("name")String name, @Param("number")String number);
	
	Integer saveUser(User user);
	
	Integer saveTeacher(Teacher teacher);

	Integer saveStudent(Student student);

	void deleteUser(int id);
	
	void deleteTeacher(int teacherId);

	void deleteStudent(int studentId);

}
